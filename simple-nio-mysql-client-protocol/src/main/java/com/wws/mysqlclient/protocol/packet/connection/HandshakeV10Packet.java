package com.wws.mysqlclient.protocol.packet.connection;

import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.common.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.Arrays;

/**
 * 握手协议
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 14:46
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake">Handshake</a>
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
    private int capabilityFlagsLower;

    /**
     * 1
     */
    private short charsetFlag;

    /**
     * 2
     */
    private int serverStatusFlag;

    /**
     * 2
     */
    private int capabilityFlagsUpper;

    /**
     * 1
     */
    private short authPluginDataLength;

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
        this.setCapabilityFlagsLower(byteBuf.readUnsignedShortLE());
        this.setCharsetFlag(byteBuf.readByte());
        this.setServerStatusFlag(byteBuf.readUnsignedShortLE());
        this.setCapabilityFlagsUpper(byteBuf.readUnsignedShortLE());
        this.setAuthPluginDataLength(byteBuf.readUnsignedByte());
        this.setReserved(MysqlByteBufUtil.readNByte(byteBuf, 10));
        byte[] bytes = MysqlByteBufUtil.readNByte(byteBuf, Math.max(13, (int) this.getAuthPluginDataLength() - 8));
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == (byte) 0) {
            i--;
        }
        this.setAuthPluginDataPart2(Arrays.copyOf(bytes, i + 1));
        this.setAuthPluginName(new String(MysqlByteBufUtil.readUtilNUL(byteBuf)));
    }

    @Override
    public ByteBuf write() {
        return null;
    }

}
