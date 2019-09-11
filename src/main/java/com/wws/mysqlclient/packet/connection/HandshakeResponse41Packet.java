package com.wws.mysqlclient.packet.connection;

import com.wws.mysqlclient.config.MysqlConfig;
import com.wws.mysqlclient.enums.CapabilityFlags;
import com.wws.mysqlclient.util.MysqlAuthUtil;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * 握手响应协议
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 19:22
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeResponse ">HandshakeResponse</a>
 **/
@Data
public class HandshakeResponse41Packet {

    /**
     * 2
     */
    private short capabilityFlagsLower;

    /**
     * 2
     */
    private short capabilityFlagsUpper;

    private int capabilityFlags;

    /**
     * 4
     */
    private int maxPacketSize;

    /**
     * 1
     */
    private byte charset;

    /**
     * 23
     */
    private byte[] reserved;

    /**
     * string[NUL]
     */
    private String username;

    /**
     * string[NUL]
     */
    private byte[] authResponse;

    /**
     * string[NUL]
     * capabilities & CLIENT_CONNECT_WITH_DB
     */
    private String database;

    /**
     * string[NUL]
     * capabilities & CLIENT_PLUGIN_AUTH
     */
    private String authPluginName;

    public static ByteBuf login(MysqlConfig config, HandshakeV10Packet handshakeV10Packet) {
        return write(getHandshakeResponsePacket(config, handshakeV10Packet));
    }

    private static ByteBuf write(HandshakeResponse41Packet handshakeResponse41Packet) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(handshakeResponse41Packet.length());
//        byteBuf.writeShortLE(handshakeResponse41Packet.getCapabilityFlagsLower());
//        byteBuf.writeShortLE(handshakeResponse41Packet.getCapabilityFlagsUpper());
        byteBuf.writeIntLE(handshakeResponse41Packet.getCapabilityFlags());

        byteBuf.writeIntLE(handshakeResponse41Packet.getMaxPacketSize());
        byteBuf.writeByte(handshakeResponse41Packet.getCharset());
        byteBuf.writeBytes(handshakeResponse41Packet.getReserved());
        MysqlByteBufUtil.writeStringNUL(byteBuf, handshakeResponse41Packet.getUsername());
        MysqlByteBufUtil.writeStringNUL(byteBuf, handshakeResponse41Packet.getAuthResponse());
        MysqlByteBufUtil.writeStringNUL(byteBuf, handshakeResponse41Packet.getDatabase());
        MysqlByteBufUtil.writeStringNUL(byteBuf, handshakeResponse41Packet.getAuthPluginName());
        return byteBuf;
    }

    private static HandshakeResponse41Packet getHandshakeResponsePacket(MysqlConfig config, HandshakeV10Packet handshakeV10Packet) {
        String username = config.getUsername();
        String password = config.getPassword();
        String database = config.getDatabase();

        HandshakeResponse41Packet handshakeResponse41Packet = new HandshakeResponse41Packet();

//        short capabilityFlagsLower = CapabilityFlags.Lower.CLIENT_SECURE_CONNECTION
////                | CapabilityFlags.Lower.CLIENT_FOUND_ROWS
////                | CapabilityFlags.Lower.CLIENT_LONG_FLAG
//                | CapabilityFlags.Lower.CLIENT_CONNECT_WITH_DB
////                | CapabilityFlags.Lower.CLIENT_IGNORE_SPACE
//                | CapabilityFlags.Lower.CLIENT_PROTOCOL_41;
//
//        short capabilityFlagsUpper = /*CapabilityFlags.Upper.CLIENT_MULTI_STATEMENTS*/
////                | CapabilityFlags.Upper.CLIENT_MULTI_RESULTS
////                | CapabilityFlags.Upper.CLIENT_PS_MULTI_RESULTS
//                 CapabilityFlags.Upper.CLIENT_PLUGIN_AUTH;

        int capabilityFlags = CapabilityFlags.CLIENT_CONNECT_WITH_DB
                | CapabilityFlags.CLIENT_PROTOCOL_41
                | CapabilityFlags.CLIENT_PLUGIN_AUTH;

        byte[] authPluginDataPart1 = handshakeV10Packet.getAuthPluginDataPart1();
        byte[] authPluginDataPart2 = handshakeV10Packet.getAuthPluginDataPart2();
        byte[] seed = new byte[authPluginDataPart1.length + authPluginDataPart2.length];
        System.arraycopy(authPluginDataPart1, 0, seed, 0, authPluginDataPart1.length);
        System.arraycopy(authPluginDataPart2, 0, seed, authPluginDataPart1.length, authPluginDataPart2.length);

//        handshakeResponse41Packet.setCapabilityFlagsLower(capabilityFlagsLower);
//        handshakeResponse41Packet.setCapabilityFlagsUpper(capabilityFlagsUpper);
        handshakeResponse41Packet.setCapabilityFlags(capabilityFlags);
        handshakeResponse41Packet.setMaxPacketSize(16 * 1024 * 1024 - 1);
        handshakeResponse41Packet.setCharset((byte) 33);
        handshakeResponse41Packet.setReserved(new byte[23]);
        handshakeResponse41Packet.setUsername(username);
        handshakeResponse41Packet.setAuthResponse(MysqlAuthUtil.cachingSHA2Password(password.getBytes(StandardCharsets.UTF_8), seed));
        handshakeResponse41Packet.setDatabase(database);
        handshakeResponse41Packet.setAuthPluginName("caching_sha2_password");

        System.out.println(handshakeResponse41Packet);
        return handshakeResponse41Packet;
    }

    private int length() {
        int len = 4 + 4 + 1 + 23;
        len += username.length() + 1;
        len += authResponse.length + 1;
        len += database.length() + 1;
        len += authPluginName.length() + 1;
        return len;
    }
}
