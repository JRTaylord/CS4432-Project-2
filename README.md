CS4432 Project 2
-
_Brady Snowden_

_James Taylor_

Installation Instructions

    To install and run this version of SimpleDB, open the project
    in either intellij or eclipse and run simpledb.server.Startup.main.
    
    To test the indexing, run IndexTesting.src.CreateTestTables.main
    followed by IndexTesting.src.IndexTests.main.

Task 2

    To implement the extensible hashing required in Task 2, we started
    by making a Bucket class to hold the index's information. The next
    step was to create the structure for the index itself. We started
    by initializing the index with two buckets, with bits of 0 and 1.
    After that we step by step implemented the inherited methods from
    the Index interface.

Task 3

    To test the functionality of each index type, we used the provided
    DataGenerator.java class to generate 5 large tables of data. We then
    followed the Task 3 instructions and queried the data with:
    "select a1,a2 from test1 where a1=57"
    We tried to query the data after joining each table with the fifth table
    but we were unable to successfully run those queries without any errors.
    The runtimes are as follows:
        sh time: 10 ms
        eh time: 2647 ms
        bt time: 4 ms
        no index time: 2 ms
    Obviously those runtimes are not the expected values, which leads us to
    believe that there is an error around Index.open that causes the wrong
    index type to be created.

Task 4

    The prerequisite amount of shotgun surgery required to complete this task
    made it nigh impossible to fully implement. What was able to be accomplished
    was the implementation of the sorted tags on the tables and the logic within
    SmartSortPlan where it checks if the relevant table's info shows if it's
    sorted and returns a basic table scan. Additionally both the SmartMerge
    classes are empty aside from extending their base classes.

Task 5

    The ExploitSortQueryPlanner SmartMergeJoins each data source it is given
    and should output the desired result. Unfortunately due to the backend side
    of task 4 being too complicated to complete, testing was not feasible.
    Although it should be theoretically faster if SmartMergeJoin is used instead
    of MergeJoin due to the program having the tables and views presorted.
    
Bugs

    The code for part 4 and 5 is not runnable nor testable because of the
    complexity of the code.