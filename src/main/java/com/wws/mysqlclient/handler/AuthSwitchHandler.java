package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.config.MysqlConfig;
import com.wws.mysqlclient.enums.AttributeKeys;
import com.wws.mysqlclient.packet.MysqlPacket;
import com.wws.mysqlclient.packet.connection.AuthSwitchRequestPacket;
import com.wws.mysqlclient.packet.connection.AuthSwitchResponsePacket;
import com.wws.mysqlclient.plugin.AuthPluginContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

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
        Byte sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY).get();
        sequenceId = (byte)(sequenceId + 1);

        MysqlConfig config = channelHandlerContext.channel().attr(AttributeKeys.CONFIG_KEY).get();
        String password = config.getPassword();

        byte[] authMethodData = authSwitchRequestPacket.getAuthMethodData();
        String authMethodName = authSwitchRequestPacket.getAuthMethodName();
        byte[] authpluginResponse = AuthPluginContext.generate(authMethodName, password.getBytes(StandardCharsets.UTF_8), authMethodData);
        AuthSwitchResponsePacket authSwitchResponsePacket = new AuthSwitchResponsePacket();
        authSwitchResponsePacket.setAuthPluginResponse(authpluginResponse);

        MysqlPacket mysqlPacket = new MysqlPacket(sequenceId, authSwitchResponsePacket.write());
        channelHandlerContext.writeAndFlush(mysqlPacket.write());
    }
}
