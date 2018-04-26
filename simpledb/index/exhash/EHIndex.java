package simpledb.index.exhash;

import javafx.scene.control.Tab;
import simpledb.index.Index;
import simpledb.query.Constant;
import simpledb.query.TableScan;
import simpledb.record.RID;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;

import java.util.ArrayList;

/**
 * Created by Brady Snowden on 4/25/2018.
 */
public class EHIndex implements Index{
    private int buckets = 1;
    private int globaldepth = 1;
    private String idxname;
    private Schema sch;
    private Transaction tx;
    private Schema globalSchema;
    private Constant searchkey = null;
    private TableScan ts = null;
    private int localdepth;
    private String bktname;

    public EHIndex(String idxname, Schema sch, Transaction tx) {
        this.idxname = idxname;
        this.sch = sch;
        this.tx = tx;
        this.globalSchema = new Schema();
        this.globalSchema.addIntField("bits");
        this.globalSchema.addStringField("filename", 30);
        this.globalSchema.addIntField("localdepth");
        TableScan tempScan = new TableScan(new TableInfo("globalTable", globalSchema), tx);
        if(!tempScan.next()) {
            tempScan.insert();
            tempScan.setInt("bits", 0);
            tempScan.setString("filename", this.idxname + 0);
            tempScan.setInt("localdepth", 1);
            tempScan.insert();
            tempScan.setInt("bits", 1);
            tempScan.setString("filename", this.idxname + 1);
            tempScan.setInt("localdepth", 1);
        }
    }

    public void beforeFirst(Constant searchkey) {
        close();
        this.searchkey = searchkey;
        TableScan tempScan = new TableScan(new TableInfo("globalTable", globalSchema), tx);
        int gsize = 0;
        while(tempScan.next())
            gsize++;
        globaldepth = (int)(Math.log(gsize)/Math.log(2));
        int bkt = searchkey.hashCode() % (int)Math.pow(2, globaldepth);
        tempScan.beforeFirst();
        while(tempScan.next())
            if(tempScan.getInt("bits") == bkt) {
                localdepth = tempScan.getInt("localdepth");
                bktname = tempScan.getString("filename");
                break;
            }
        tempScan.close();
        TableInfo ti = new TableInfo(bktname, sch);
        ts = new TableScan(ti, tx);
    }

    public boolean next() {
        while(ts.next())
            if(ts.getVal("dataval").equals(searchkey))
                return true;
        return false;
    }

    public RID getDataRid() {
        int blknum = ts.getInt("block");
        int id = ts.getInt("id");
        return new RID(blknum, id);
    }

    public void insert(Constant dataval, RID datarid) {
        beforeFirst(dataval);
        int size = 0;
        while(ts.next())
            size++;
        while(size >= 4) {
            TableScan temp = new TableScan(new TableInfo("globalTable", globalSchema), tx);
            int gsize = 0;
            while(temp.next())
                gsize++;
            globaldepth = (int)(Math.log(gsize)/Math.log(2));
            if(localdepth == globaldepth) {
                globaldepth++;
                ArrayList<Bucket> list = new ArrayList<>();
                temp.beforeFirst();
                while(temp.next())
                    list.add(new Bucket(temp.getInt("bits")+(int)Math.pow(2, globaldepth-1), temp.getString("filename"), temp.getInt("localdepth")));
                for(Bucket b: list) {
                    temp.insert();
                    temp.setInt("bits", b.getBits());
                    temp.setString("filename", b.getFilename());
                    temp.setInt("localdepth", b.getLocaldepth());
                }
            }
            localdepth++;

            String bktname1 = bktname;
            TableScan scan1 = new TableScan(new TableInfo(bktname1, sch), tx);
            int bkt2bits = (searchkey.hashCode() % (int)Math.pow(2, localdepth-1)) + (int)Math.pow(2, localdepth-1);
            String bktname2 = idxname + bkt2bits;
            TableScan scan2 = new TableScan(new TableInfo(bktname2, sch), tx);
            while(scan1.next())
                if(scan1.getVal("dataval").hashCode()%(int)Math.pow(2, localdepth) == bkt2bits) {
                    scan2.insert();
                    scan2.setInt("block", scan1.getInt("block"));
                    scan2.setInt("id", scan1.getInt("id"));
                    scan2.setVal("dataval", scan1.getVal("dataval"));
                    scan1.delete();
                }
            scan1.close();
            scan2.close();

            temp.beforeFirst();
            int sigbit = (int)Math.pow(2, localdepth-1);
//            while(temp.next()) {
//                int bucket = temp.getInt("bits");
//                if(bucket % sigbit == searchkey.hashCode() % sigbit){
//                    temp.setInt("localdepth", localdepth);
//                    if(bucket/sigbit>=1)
//                        temp.setString("filename", bktname2);
//                    else
//                        temp.setString("filename", bktname1);
//                }
//            }
            while (temp.next()){
                int bucket = temp.getInt("bits");
                if(bucket % sigbit == searchkey.hashCode() % sigbit){
                    temp.setInt("localdepth", localdepth);
                    if(bucket/sigbit>=1){
                        temp.setString("filename", bktname2);
                    } else {
                        temp.setString("filename", bktname1);
                    }
                }
            }
            temp.close();
            beforeFirst(dataval);
            size = 0;
            while(ts.next())
                size++;
        }
        ts.insert();
        ts.setInt("block", datarid.blockNumber());
        ts.setInt("id", datarid.id());
        ts.setVal("dataval", dataval);
    }

    public void delete(Constant dataval, RID datarid) {
        beforeFirst(dataval);
        while(next())
            if(getDataRid().equals(datarid)) {
                ts.delete();
                return;
            }
    }

    public void close() {
        if(ts != null)
            ts.close();
    }

    @Override
    public String toString() {
        String outstring = "";
        TableScan scan = new TableScan(new TableInfo("globalTable", globalSchema), tx);
        while(scan.next()) {
            outstring += "\n" + Integer.toBinaryString(scan.getInt("bits"));
            TableScan scan2 = new TableScan(new TableInfo(scan.getString("filename"), sch), tx);
            while(scan2.next())
                outstring += "\n\t" + scan2.getVal("dataval").hashCode() + "\t" + Integer.toBinaryString(scan2.getVal("dataval").hashCode());
            scan2.close();
        }
        scan.close();
        return outstring + "\n--------------\n";
    }
}
