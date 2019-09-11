package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.packet.connection.AuthSwitchRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 切换验证方式处理器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 19:16
 **/
public class AuthSwitchHandler extends SimpleChannelInboundHandler<AuthSwitchRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AuthSwitchRequestPacket authSwitchRequestPacket) throws Exception {

    }
}
