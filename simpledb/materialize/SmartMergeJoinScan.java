package simpledb.materialize;

import simpledb.query.Scan;

/**
 * Project 2
 */
public class SmartMergeJoinScan extends MergeJoinScan {

    public SmartMergeJoinScan(Scan s1, SmartSortScan s2, String fldname1, String fldname2){
        super(s1, s2, fldname1, fldname2);
    }
}
