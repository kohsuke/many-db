import org.netbeans.insane.scanner.ScannerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class App {
    public static void main(String[] args) throws Exception {
//        new App() {
//            protected DBHolder create(int i) {
//                return new Derby(i);
//            }
//        }.test(10); // derby is like 3MB/db (according to Insane), but inserts are really slow. YJP also reports about 3MB/db

//        System.out.println("==================");
//        new App() {
//            protected DBHolder create(int i) {
//                return new H2(i);
//            }
//        }.test(10); // H2 is about 420K/db (according to Insane), 720KB according to YJP

        System.out.println("==================");
        new App() {
            protected DBHolder create(int i) {
                return new HSQLDB(i);
            }
        }.test(10); // 2MB/db (according to Insane) 1.4M according to YJP
    }

    /**
     * Keeps connections to a populated databasse and measure footprint.
     * @param total
     */
    void test(int total) throws Exception {
        Set<Connection> live = new HashSet<Connection>();
        List<DBHolder> r = new ArrayList<DBHolder>();
        for (int i=0; i< total; i++) {
            long start = System.currentTimeMillis();
            DBHolder db = create(i);
            db.create();
            r.add(db);
            Connection con = db.connect();
            live.add(con);
            System.out.printf("#%d: Retention size=%d, took=%dms\n",
                    i, ScannerUtils.recursiveSizeOf(Collections.singleton(con),null), System.currentTimeMillis()-start);
        }

        hang();

        for (Connection c : live) {
            c.close();
        }
    }

    private void hang() throws IOException {
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    protected abstract DBHolder create(int i);
}
