package com.wws.mysqlclient.packet.connection;

import com.wws.mysqlclient.config.MysqlConfig;
import com.wws.mysqlclient.enums.CapabilityFlags;
import com.wws.mysqlclient.packet.BaseLengthPacket;
import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.plugin.AuthPluginContext;
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
public class HandshakeResponse41Packet implements BaseSeriablizablePacket, BaseLengthPacket {

    /**
     * 4
     */
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

    public static HandshakeResponse41Packet getHandshakeResponsePacket(MysqlConfig config, HandshakeV10Packet handshakeV10Packet) {
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

    @Override
    public int length() {
        int len = 4 + 4 + 1 + 23;
        len += username.length() + 1;
        len += authResponse.length + 1;
        len += database.length() + 1;
        len += authPluginName.length() + 1;
        return len;
    }

    @Override
    public void read(ByteBuf byteBuf) {

    }

    @Override
    public ByteBuf write() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(this.length());
        byteBuf.writeIntLE(this.getCapabilityFlags());

        byteBuf.writeIntLE(this.getMaxPacketSize());
        byteBuf.writeByte(this.getCharset());
        byteBuf.writeBytes(this.getReserved());
        MysqlByteBufUtil.writeStringNUL(byteBuf, this.getUsername());
        MysqlByteBufUtil.writeLengthEncodedString(byteBuf, this.getAuthResponse());
        MysqlByteBufUtil.writeStringNUL(byteBuf, this.getDatabase());
        MysqlByteBufUtil.writeStringNUL(byteBuf, this.getAuthPluginName());
        return byteBuf;
    }
}
