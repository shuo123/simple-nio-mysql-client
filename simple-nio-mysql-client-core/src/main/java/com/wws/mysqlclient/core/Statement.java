package com.wws.mysqlclient.core;

import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.core.network.handler.InboundPrintHandler;
import com.wws.mysqlclient.core.network.handler.command.ComQueryResponseDecoder;
import com.wws.mysqlclient.core.network.handler.connection.AuthDecoder;
import com.wws.mysqlclient.core.network.handler.connection.AuthSwitchHandler;
import com.wws.mysqlclient.protocol.packet.command.ComQueryPacket;
import com.wws.mysqlclient.protocol.packet.command.ComQueryResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.concurrent.Future;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 15:25
 **/
public class Statement {

    private Channel channel;

    public Statement(Channel channel) {
        this.channel = channel;
    }

    public Future<ComQueryResponsePacket> executeQuery(String sql){
        ComQueryPacket comQueryPacket = new ComQueryPacket();
        comQueryPacket.setSql(sql);

        channel.pipeline().remove(AuthDecoder.class);
        channel.pipeline().remove(AuthSwitchHandler.class);
        channel.pipeline().remove(InboundPrintHandler.class);

        channel.pipeline().addLast(new ComQueryResponseDecoder());

        FuturePacket<ComQueryResponsePacket> futurePacket = new FuturePacket<>();
        ResponsePacketFuture<ComQueryResponsePacket> future = new ResponsePacketFuture<>(channel, futurePacket);
        channel.attr(AttributeKeys.COM_QUERY_RESPONSE_PACKET_KEY).set(futurePacket);
        channel.attr(AttributeKeys.SEQUENCE_ID_KEY).set((byte) -1);

        channel.writeAndFlush(comQueryPacket);
        return future;
    }
}
