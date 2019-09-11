package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.packet.connection.HandshakeV10Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-06 17:54
 **/
public class RecieveHandler extends SimpleChannelInboundHandler<HandshakeV10Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HandshakeV10Packet handshakeV10Packet) throws Exception {
        System.out.println(handshakeV10Packet);
    }

}
