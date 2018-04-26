import simpledb.remote.SimpleDriver;

import java.sql.*;

/**
 * Created by Brady Snowden on 4/25/2018.
 */
public class IndexTests {
    public static void main(String[] args) {
        long test1start = 0;
        long test1elapsed = 0;
        long test2start = 0;
        long test2elapsed = 0;
        long test3start = 0;
        long test3elapsed = 0;
        long test4start = 0;
        long test4elapsed = 0;

        long join1start = 0;
        long join1elapsed = 0;
        long join2start = 0;
        long join2elapsed = 0;
        long join3start = 0;
        long join3elapsed = 0;
        long join4start = 0;
        long join4elapsed = 0;

        Connection conn = null;
        Driver d = new SimpleDriver();
        String host = "localhost"; //you may change it if your SimpleDB server is running on a different machine
        String url = "jdbc:simpledb://" + host;
        try {
            conn = d.connect(url, null);
            Statement stmt = conn.createStatement();
            test1start = System.currentTimeMillis();
            stmt.executeQuery("select a1,a2 from test1 where a1=57"); //sh
            test1elapsed = System.currentTimeMillis() - test1start;
            test2start = System.currentTimeMillis();
            stmt.executeQuery("select a1,a2 from test2 where a1=57"); //eh
            test2elapsed = System.currentTimeMillis() - test2start;
            test3start = System.currentTimeMillis();
            stmt.executeQuery("select a1,a2 from test3 where a1=57"); //bt
            test3elapsed = System.currentTimeMillis() - test3start;
            test4start = System.currentTimeMillis();
            stmt.executeQuery("select a1,a2 from test4 where a1=57"); //no index
            test4elapsed = System.currentTimeMillis() - test4start;

//            join1start = System.currentTimeMillis();
//            stmt.executeQuery("select a1,a2 from test5,test1 where a1 = test1.a1"); //sh
//            join1elapsed = System.currentTimeMillis() - join1start;
//            join2start = System.currentTimeMillis();
//            stmt.executeQuery("select a1,a2 from test5,test2 where a1 = test2.a1"); //eh
//            join2elapsed = System.currentTimeMillis() - join2start;
//            join3start = System.currentTimeMillis();
//            stmt.executeQuery("select a1,a2 from test5,test3 where a1 = test3.a1"); //bt
//            join3elapsed = System.currentTimeMillis() - join3start;
//            join4start = System.currentTimeMillis();
//            stmt.executeQuery("select a1,a2 from test5,test4 where a1 = test4.a1"); //no index
//            join4elapsed = System.currentTimeMillis() - join4start;
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        System.out.println("sh time: " + test1elapsed);
        System.out.println("eh time: " + test2elapsed);
        System.out.println("bt time: " + test3elapsed);
        System.out.println("no index time: " + test4elapsed);
        System.out.println("--------------");
        System.out.println("sh join time: " + join1elapsed);
        System.out.println("eh join time: " + join2elapsed);
        System.out.println("bt join time: " + join3elapsed);
        System.out.println("no index join time: " + join4elapsed);
    }
}
