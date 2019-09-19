import com.wws.mysqlclient.core.Connection;
import com.wws.mysqlclient.core.ConnectionFactory;
import com.wws.mysqlclient.core.ResponsePacketFuture;
import com.wws.mysqlclient.core.Statement;
import com.wws.mysqlclient.core.config.MysqlConfig;
import com.wws.mysqlclient.protocol.packet.command.ComQueryResponsePacket;
import com.wws.mysqlclient.protocol.packet.command.ResultsetRowPacket;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 16:22
 **/
public class ConnectionTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        MysqlConfig config = new MysqlConfig();
        config.setDatabase("test");
        config.setUsername("root");
        config.setPassword("root");
        config.setHost("127.0.0.1");
        config.setPort(3306);

        ConnectionFactory factory = ConnectionFactory.getInstance();

        Connection connection = factory.getConnection(config);
        Future future = connection.connect();
        future.sync();
        Statement statement = connection.createStatement();
        ResponsePacketFuture<ComQueryResponsePacket> packetFuture = (ResponsePacketFuture<ComQueryResponsePacket>) statement.executeQuery("select * from user");
        ComQueryResponsePacket comQueryResponsePacket = packetFuture.get();
        System.out.println(comQueryResponsePacket);
        List<ResultsetRowPacket> resultsetRowPacketList = comQueryResponsePacket.getResultsetRowPacketList();
        for (ResultsetRowPacket resultsetRowPacket : resultsetRowPacketList) {
            List<String> row = resultsetRowPacket.getRow();
            System.out.println(row.get(1));
        }

        connection.close().sync();
        factory.destory();

    }

}
