import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-06 17:56
 **/
public class MysqlTest {

    @Test
    public void test() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307?useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
        connection.close();
    }

}
