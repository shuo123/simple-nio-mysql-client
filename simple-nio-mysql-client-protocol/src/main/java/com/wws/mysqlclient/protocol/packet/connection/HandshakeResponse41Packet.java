package com.wws.mysqlclient.protocol.packet.connection;

import com.wws.mysqlclient.protocol.packet.BaseLengthPacket;
import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.common.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;

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
