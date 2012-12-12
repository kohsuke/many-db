import org.h2.engine.ConnectionInfo;
import org.h2.engine.Engine;
import org.h2.jdbc.JdbcConnection;

import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Kohsuke Kawaguchi
 */
public class H2 extends DBHolder {
    public H2(int id) {
        super(id);
    }

    public JdbcConnection connect() throws SQLException {
        JdbcConnection con = new JdbcConnection("jdbc:h2:/tmp/h2/data" + id, new Properties());
        con.setAutoCommit(true);
        return con;
    }

    void foo() throws SQLException {
        ConnectionInfo ci = new ConnectionInfo("jdbc:h2:/tmp/foo", new Properties());
        JdbcConnection con = new JdbcConnection(Engine.getInstance().createSession(ci), null, ci.getURL());
        con.close();
    }
}
