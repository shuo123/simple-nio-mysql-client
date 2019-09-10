package handler;

import config.MysqlConfig;
import enums.AttributeKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import packet.HandshakePacket;
import packet.HandshakeResponsePacket;
import packet.MysqlPacket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 握手协议处理器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-10 14:59
 **/
public class HandshakeHandler extends SimpleChannelInboundHandler<HandshakePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HandshakePacket handshakePacket) throws Exception {
        AtomicInteger sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY).get();

        MysqlConfig config = channelHandlerContext.channel().attr(AttributeKeys.CONFIG_KEY).get();
        ByteBuf payload = HandshakeResponsePacket.login(config, handshakePacket);
        MysqlPacket mysqlPacket = new MysqlPacket((byte)sequenceId.incrementAndGet(), payload);
        ByteBuf byteBuf = mysqlPacket.write();
        channelHandlerContext.writeAndFlush(byteBuf);
    }
}