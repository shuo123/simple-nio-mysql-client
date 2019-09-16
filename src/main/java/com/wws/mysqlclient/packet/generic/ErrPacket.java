package com.wws.mysqlclient.packet.generic;

import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * err报文
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html">ERR_Packet</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 10:53
 **/
@Data
public class ErrPacket implements BaseSeriablizablePacket {
    /**
     * 1
     * 0xff
     */
    private short header = 0xff;

    private int errorCode;

    private String sqlStateMarker;

    private String sqlState;

    private String errorMessage;

    @Override
    public void read(ByteBuf byteBuf) {
        this.setHeader(byteBuf.readUnsignedByte());
        this.setErrorCode(byteBuf.readUnsignedShortLE());
        this.setSqlState(new String(MysqlByteBufUtil.readNByte(byteBuf,1)));
        this.setSqlStateMarker(new String(MysqlByteBufUtil.readNByte(byteBuf,5)));
        this.setErrorMessage(new String(MysqlByteBufUtil.readUtilEOF(byteBuf)));
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
