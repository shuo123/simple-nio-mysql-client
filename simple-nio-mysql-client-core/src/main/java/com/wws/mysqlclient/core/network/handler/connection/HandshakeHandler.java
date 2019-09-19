package com.wws.mysqlclient.core.network.handler.connection;

import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.core.config.MysqlConfig;
import com.wws.mysqlclient.core.network.handler.InboundPrintHandler;
import com.wws.mysqlclient.common.plugin.AuthPluginContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.wws.mysqlclient.protocol.packet.connection.HandshakeV10Packet;
import com.wws.mysqlclient.protocol.packet.connection.HandshakeResponse41Packet;
import com.wws.mysqlclient.protocol.enums.CapabilityFlags;

import java.nio.charset.StandardCharsets;

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

        MysqlConfig config = channelHandlerContext.channel().attr(AttributeKeys.CONFIG_KEY).get();
        HandshakeResponse41Packet handshakeResponsePacket = getHandshakeResponsePacket(config, handshakeV10Packet);
        channelHandlerContext.writeAndFlush(handshakeResponsePacket);

        channelHandlerContext.pipeline().remove(HandshakeHandler.class);
        channelHandlerContext.pipeline().remove(HandshakeDecoder.class);
        channelHandlerContext.pipeline().remove(InboundPrintHandler.class);
        channelHandlerContext.pipeline().addLast(new AuthDecoder());
        channelHandlerContext.pipeline().addLast(new InboundPrintHandler());
        channelHandlerContext.pipeline().addLast(new AuthSwitchHandler());
    }

    private static HandshakeResponse41Packet getHandshakeResponsePacket(MysqlConfig config, HandshakeV10Packet handshakeV10Packet) {
        String username = config.getUsername();
        String password = config.getPassword();
        String database = config.getDatabase();

        HandshakeResponse41Packet handshakeResponse41Packet = new HandshakeResponse41Packet();

        int capabilityFlags = CapabilityFlags.CLIENT_LONG_FLAG
                | CapabilityFlags.CLIENT_FOUND_ROWS
                | CapabilityFlags.CLIENT_LONG_PASSWORD
                | CapabilityFlags.CLIENT_CONNECT_WITH_DB
                | CapabilityFlags.CLIENT_LOCAL_FILES
                | CapabilityFlags.CLIENT_PROTOCOL_41
                | CapabilityFlags.CLIENT_TRANSACTIONS
                | CapabilityFlags.CLIENT_SECURE_CONNECTION
                | CapabilityFlags.CLIENT_MULTI_RESULTS
                | CapabilityFlags.CLIENT_PLUGIN_AUTH
                | CapabilityFlags.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
                | CapabilityFlags.CLIENT_DEPRECATE_EOF;

        byte[] authPluginDataPart1 = handshakeV10Packet.getAuthPluginDataPart1();
        byte[] authPluginDataPart2 = handshakeV10Packet.getAuthPluginDataPart2();
        byte[] seed = new byte[authPluginDataPart1.length + authPluginDataPart2.length];
        System.arraycopy(authPluginDataPart1, 0, seed, 0, authPluginDataPart1.length);
        System.arraycopy(authPluginDataPart2, 0, seed, authPluginDataPart1.length, authPluginDataPart2.length);

        handshakeResponse41Packet.setCapabilityFlags(capabilityFlags);
        handshakeResponse41Packet.setMaxPacketSize(16 * 1024 * 1024 - 1);
        handshakeResponse41Packet.setCharset((byte) 33);
        handshakeResponse41Packet.setReserved(new byte[23]);
        handshakeResponse41Packet.setUsername(username);
        handshakeResponse41Packet.setAuthResponse(AuthPluginContext.generate("caching_sha2_password", password.getBytes(StandardCharsets.UTF_8), seed));
        handshakeResponse41Packet.setDatabase(database);
        handshakeResponse41Packet.setAuthPluginName("caching_sha2_password");

        return handshakeResponse41Packet;
    }

}
