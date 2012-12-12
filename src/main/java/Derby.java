import org.apache.derby.jdbc.EmbeddedDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Kohsuke Kawaguchi
 */
public class Derby extends DBHolder {
    public Derby(int id) {
        super(id);
    }

    @Override
    public Connection connect() throws SQLException {
        // there must be a better way to do this
        System.setProperty("derby.system.home","/tmp/derby/");
        Connection con = new EmbeddedDriver().connect("jdbc:derby:derbyDB"+id+";create=true", new Properties());
        con.setAutoCommit(false);
        // auto-commmit in derby is very slow: http://stackoverflow.com/questions/8745629/apache-derby-inserts-are-slow
        return con;
    }
}
