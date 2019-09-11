package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.enums.AttributeKeys;
import com.wws.mysqlclient.config.MysqlConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.wws.mysqlclient.packet.connection.HandshakeV10Packet;
import com.wws.mysqlclient.packet.connection.HandshakeResponse41Packet;
import com.wws.mysqlclient.packet.MysqlPacket;

/**
 * 握手协议处理器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-10 14:59
 **/
public class HandshakeHandler extends SimpleChannelInboundHandler<HandshakeV10Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HandshakeV10Packet handshakeV10Packet) throws Exception {
        Byte sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY).get();
        sequenceId = (byte)(sequenceId + 1);

        MysqlConfig config = channelHandlerContext.channel().attr(AttributeKeys.CONFIG_KEY).get();
        ByteBuf payload = HandshakeResponse41Packet.login(config, handshakeV10Packet);
        MysqlPacket mysqlPacket = new MysqlPacket(sequenceId, payload);
        ByteBuf byteBuf = mysqlPacket.write();
        channelHandlerContext.writeAndFlush(byteBuf);

        channelHandlerContext.pipeline().remove(HandshakeHandler.class);
        channelHandlerContext.pipeline().remove(HandshakeDecoder.class);
        channelHandlerContext.pipeline().addLast(new AuthDecoder());
    }
}