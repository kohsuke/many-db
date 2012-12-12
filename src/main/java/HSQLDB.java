import org.hsqldb.DatabaseURL;
import org.hsqldb.jdbc.JDBCConnection;
import org.hsqldb.persist.HsqlProperties;

import java.sql.SQLException;
import java.util.Random;

/**
 * @author Kohsuke Kawaguchi
 */
public class HSQLDB extends DBHolder {
    public HSQLDB(int id) {
        super(id);
    }

    public JDBCConnection connect() throws SQLException {
        HsqlProperties props = DatabaseURL.parseURL("jdbc:hsqldb:file:/tmp/hsqldb/data" + id, true, false);
        props.setProperty("hsqldb.default_table_type","cached");
        props.setProperty("hsqldb.script_format",3);
        props.setProperty("enforce_size",false);
        JDBCConnection con = new JDBCConnection(props);
        con.setAutoCommit(true);
        return con;
    }

    private static String randomString(int len) {
        StringBuilder b = new StringBuilder(len);
        for (int i=0; i<len; i++)
            b.append('a'+RAND.nextInt(26));
        return b.toString();
    }

    private static int randomInt(int n) {
        return RAND.nextInt(n);
    }

    private static Random RAND = new Random();
}
