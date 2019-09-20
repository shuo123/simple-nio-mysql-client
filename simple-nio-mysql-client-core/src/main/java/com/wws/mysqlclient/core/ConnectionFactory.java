package com.wws.mysqlclient.core;

import com.wws.mysqlclient.core.config.MysqlConfig;
import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.core.network.handler.*;
import com.wws.mysqlclient.core.network.handler.connection.HandshakeDecoder;
import com.wws.mysqlclient.core.network.handler.connection.HandshakeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;

import java.nio.ByteOrder;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 14:18
 **/
public class ConnectionFactory {

    private static volatile ConnectionFactory INSTANCE;

    private Bootstrap bootstrap;
    private NioEventLoopGroup group;
    private EventExecutor eventExecutor;

    private ConnectionFactory() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 16 * 1024 * 1024, 0, 3, 1, 0, true))
                                .addLast(new MysqlPacketEecoder())
                                .addLast(new BaseSerialablePacketEncoder())
                                .addLast(new OutboundPrintHandler())
                                .addLast(new MysqlPacketDecoder())
                                .addLast(new HandshakeDecoder())
                                .addLast(new InboundPrintHandler())
                                .addLast(new HandshakeHandler())
                        ;
                    }
                });
        eventExecutor = new DefaultEventExecutor();
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ConnectionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConnectionFactory();
                }
            }
        }
        return INSTANCE;
    }

    public Connection getConnection(MysqlConfig config) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(config.getHost(), config.getPort()).sync();
        Channel channel = channelFuture.channel();
        channel.attr(AttributeKeys.CONFIG_KEY).setIfAbsent(config);
        channel.attr(AttributeKeys.CONNECTION).setIfAbsent(new DefaultPromise<>(new DefaultEventExecutor()));
        channel.attr(AttributeKeys.EVENTEXECUTOR).setIfAbsent(eventExecutor);
        return new Connection(channel);
    }

    public void destory() {
        group.shutdownGracefully();
    }

}
