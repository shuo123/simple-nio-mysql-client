package com.wws.mysqlclient.packet.connection;

import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * 握手协议
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake">Handshake</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 14:46
 **/
@Data
public class HandshakeV10Packet implements BaseSeriablizablePacket {

    /**
     * 1
     */
    private byte protocolVersion;

    /**
     * string[NUL]
     */
    private String serverVersion;

    /**
     * 4
     */
    private int connectionId;

    /**
     * 8
     */
    private byte[] authPluginDataPart1;

    /**
     * 2
     */
    private short capabilityFlagsLower;

    /**
     * 1
     */
    private byte charsetFlag;

    /**
     * 2
     */
    private short serverStatusFlag;

    /**
     * 2
     */
    private short capabilityFlagsUpper;

    /**
     * 1
     */
    private byte authPluginDataLength;

    /**
     * 10
     */
    private byte[] reserved;

    /**
     * len=MAX(13, length of auth-plugin-data - 8)
     */
    private byte[] authPluginDataPart2;

    /**
     * string[NUL]
     */
    private String authPluginName;

    @Override
    public void read(ByteBuf byteBuf) {
        this.setProtocolVersion(byteBuf.readByte());
        this.setServerVersion(new String(MysqlByteBufUtil.readUtilNUL(byteBuf)));
        this.setConnectionId(byteBuf.readIntLE());
        this.setAuthPluginDataPart1(MysqlByteBufUtil.readNByte(byteBuf, 8));
        byteBuf.skipBytes(1);
        this.setCapabilityFlagsLower(byteBuf.readShortLE());
        this.setCharsetFlag(byteBuf.readByte());
        this.setServerStatusFlag(byteBuf.readShortLE());
        this.setCapabilityFlagsUpper(byteBuf.readShortLE());
        this.setAuthPluginDataLength(byteBuf.readByte());
        this.setReserved(MysqlByteBufUtil.readNByte(byteBuf, 10));
        this.setAuthPluginDataPart2(MysqlByteBufUtil.readNByte(byteBuf, Math.max(13, (int) this.getAuthPluginDataLength() - 8)));
        this.setAuthPluginName(new String(MysqlByteBufUtil.readUtilNUL(byteBuf)));
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
