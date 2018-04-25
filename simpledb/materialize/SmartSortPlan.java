package simpledb.materialize;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.query.TableScan;
import simpledb.tx.Transaction;

import java.util.List;

/**
 * Project 2
 */
public class SmartSortPlan extends SortPlan{

    public SmartSortPlan(Plan p, List<String> sortfields, Transaction tx){
        super(p, sortfields, tx);
    }

    /**
     *
     * @return
     */
    @Override
    public Scan open(){
        Scan src = p.open();
        List<TempTable> runs = splitIntoRuns(src);
        src.close();
        while (runs.size() > 2)
            runs = doAMergeIteration(runs);
        if(runs.size()==1){
            if(runs.get(0).getTableInfo().getSorted()){
                return new TableScan(runs.get(0).getTableInfo(), tx);
            }
        }
        return new SmartSortScan(runs, comp);
    }
}
