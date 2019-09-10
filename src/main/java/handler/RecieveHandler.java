package handler;

import packet.HandshakePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-06 17:54
 **/
public class RecieveHandler extends SimpleChannelInboundHandler<HandshakePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HandshakePacket handshakePacket) throws Exception {
        System.out.println(handshakePacket);
    }

}
