package simpledb.materialize;

import simpledb.query.Plan;
import simpledb.tx.Transaction;

/**
 * Project 2
 */
public class SmartMergeJoinPlan extends MergeJoinPlan {

    public SmartMergeJoinPlan(Plan p1, Plan p2, String fldname1, String fldname2, Transaction tx){
        super(p1, p2, fldname1, fldname2, tx);
    }
}
