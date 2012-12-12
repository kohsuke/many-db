import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class DBHolder {
    protected final int id;

    public DBHolder(int id) {
        this.id = id;
    }

    public void create() throws Exception {
        Connection con = connect();

        if (!con.getMetaData().getTables(null,null,"DATA",null).next()) {
            // no data created yet
            Statement s = con.createStatement();
            s.execute("create table DATA (package VARCHAR(80), class VARCHAR(80), method VARCHAR(80), error INT, failure INT, time BIGINT)");
            s.close();
        }

        ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM DATA");
        rs.next();
        if (rs.getInt(1)==0) {
            System.out.println("Filling data");
            PreparedStatement ps = con.prepareStatement("INSERT INTO DATA (package,class,method,error,failure,time) VALUES (?,?,?,?,?,?)");
            for (int j=0; j<10000; j++) {
                ps.setString(1, randomString(32));
                ps.setString(2, randomString(32));
                ps.setString(3, randomString(32));
                ps.setInt(4, randomInt(1000));
                ps.setInt(5, randomInt(1000));
                ps.setLong(6, System.currentTimeMillis());
                ps.execute();
            }
            ps.close();

//            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            ResultSet rs = s.executeQuery("SELECT * FROM DATA");
//            for (int j=0; j<10000; j++) {
//                rs.moveToInsertRow();
//                rs.updateString("package", randomString(32));
//                rs.updateString("class", randomString(32));
//                rs.updateString("method", randomString(32));
//                rs.updateInt("error", randomInt(1000));
//                rs.updateInt("failure", randomInt(1000));
//                rs.updateLong("time", System.currentTimeMillis());
//                rs.insertRow();
//            }
//            s.close();
        }

        con.commit();
        con.close();
    }

    public abstract Connection connect() throws SQLException;

    private static String randomString(int len) {
        StringBuilder b = new StringBuilder(len);
        for (int i=0; i<len; i++)
            b.append((char)('a'+RAND.nextInt(26)));
        return b.toString();
    }

    private static int randomInt(int n) {
        return RAND.nextInt(n);
    }

    private static Random RAND = new Random();
}
