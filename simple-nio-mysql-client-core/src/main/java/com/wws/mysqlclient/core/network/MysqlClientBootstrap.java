package com.wws.mysqlclient.core.network;

import com.wws.mysqlclient.core.config.MysqlConfig;
import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.core.network.handler.*;
import com.wws.mysqlclient.core.network.handler.connection.HandshakeDecoder;
import com.wws.mysqlclient.core.network.handler.connection.HandshakeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

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
                }).connect("127.0.0.1", 3306)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功");
                    } else {
                        System.out.println("连接失败:" + future.cause());
                    }
                });
        ChannelFuture channelFuture1 = bootstrap.connect("127.0.0.1", 3306)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功");
                    } else {
                        System.out.println("连接失败:" + future.cause());
                    }
                });
        System.out.println(channelFuture.channel().id());
        System.out.println(channelFuture1.channel().id());
        channelFuture.channel().attr(AttributeKeys.CONFIG_KEY).setIfAbsent(config);
    }

}
