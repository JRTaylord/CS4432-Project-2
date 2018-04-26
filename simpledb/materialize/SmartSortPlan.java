package simpledb.materialize;

import simpledb.metadata.MetadataMgr;
import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.query.TableScan;
import simpledb.tx.Transaction;

import java.util.List;

/**
 * Project 2
 */
public class SmartSortPlan extends SortPlan{
    private String tableName;

    public SmartSortPlan(Plan p, List<String> sortfields, Transaction tx, String tableName){
        super(p, sortfields, tx);
        this.tableName = tableName;
    }

    /**
     *
     * @return
     */
    @Override
    public Scan open(){

        //Project 2: finds if the table is already sort through the MetadataMgr
        boolean sorted = false;
        MetadataMgr mdm = new MetadataMgr(false, tx);
        sorted = mdm.getTableInfo(tableName, tx).getSorted();

        if(sorted){
            return new TableScan(mdm.getTableInfo(tableName, tx), tx);
        }

        Scan src = p.open();
        List<TempTable> runs = splitIntoRuns(src);
        src.close();
        while (runs.size() > 2)
            runs = doAMergeIteration(runs);
        return new SmartSortScan(runs, comp);
    }
}
