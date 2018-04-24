package simpledb.materialize;

import java.util.List;

/**
 * Project 2
 */
public class SmartSortScan extends SortScan {

    public SmartSortScan(List<TempTable> runs, RecordComparator comp){
        super(runs, comp);
    }
}
