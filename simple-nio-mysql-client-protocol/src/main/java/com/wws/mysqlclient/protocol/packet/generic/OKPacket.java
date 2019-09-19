package com.wws.mysqlclient.protocol.packet.generic;

import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.common.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * ok报文
 * @see <a href="https://dev.mysql.com/doc/internals/en/packet-OK_Packet.html" >OK_Packet</a>
 *
 * OK：header= 0，数据包长度> 7
 *
 * EOF：header= 0xfe，数据包长度<9
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 10:37
 **/
@Data
public class OKPacket implements BaseSeriablizablePacket {

    /**
     * INT <1>
     * [00]或[fe]OK包头
     */
    private short header;

    /**
     * INT <lenenc>
     * 受影响的行
     */
    private long affectRow;

    /**
     * INT <lenenc>
     * 最后一个插入ID
     */
    private long lastInsertId;

    /**
     * INT <2>
     * 状态标志
     */
    private short statusFlags;

    /**
     * INT <2>
     * 警告数量
     */
    private short warnings;

    /**
     * string<EOF>
     * 服务器状态信息
     */
    private String info;

    @Override
    public void read(ByteBuf byteBuf) {
        this.setHeader(byteBuf.readUnsignedByte());
        this.setAffectRow(MysqlByteBufUtil.readLengthEncodedInteger(byteBuf));
        this.setLastInsertId(MysqlByteBufUtil.readLengthEncodedInteger(byteBuf));
        this.setStatusFlags(byteBuf.readShortLE());
        this.setWarnings(byteBuf.readShortLE());
        this.setInfo(new String(MysqlByteBufUtil.readUtilEOF(byteBuf)));
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
