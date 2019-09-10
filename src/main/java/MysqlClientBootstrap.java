import config.MysqlConfig;
import enums.AttributeKeys;
import handler.HandshakeDecoder;
import handler.HandshakeHandler;
import handler.MysqlPacketDecoder;
import handler.RecieveHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-06 19:04
 **/
public class MysqlClientBootstrap {

    public static void main(String[] args) {

        MysqlConfig config = new MysqlConfig();
        config.setDatabase("test");
        config.setUsername("root");
        config.setPassword("root");

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel){
                        socketChannel.pipeline().addLast(new MysqlPacketDecoder())
                                .addLast(new HandshakeDecoder())
//                                .addLast(new RecieveHandler())
                                .addLast(new HandshakeHandler());
                    }
                }).connect("127.0.0.1", 3306)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功");
                    } else {
                        System.out.println("连接失败:" + future.cause());
                    }
                });
        channelFuture.channel().attr(AttributeKeys.CONFIG_KEY).setIfAbsent(config);
    }

}
