package com.wws.mysqlclient.packet.command;

import com.wws.mysqlclient.packet.BaseLengthPacket;
import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-query-response.html#packet-Protocol::ColumnDefinition41">ColumnDefinition41</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-18 19:39
 **/
@Data
public class ColumnDefinition41Packet implements BaseSeriablizablePacket, BaseLengthPacket {

    private String catalog;

    private String schema;

    private String table;

    private String orgTable;

    private String name;

    private String orgName;

    private long nextLength;

    private int characterSet;

    private long columnLength;

    private short columnType;

    private int flags;

    private byte decimals;


    @Override
    public int length() {
        return 0;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.setCatalog(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setSchema(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setTable(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setOrgTable(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setName(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setOrgName(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        this.setNextLength(MysqlByteBufUtil.readLengthEncodedInteger(byteBuf));
        this.setCharacterSet(byteBuf.readUnsignedShortLE());
        this.setColumnLength(byteBuf.readUnsignedIntLE());
        this.setColumnType(byteBuf.readUnsignedByte());
        this.setFlags(byteBuf.readUnsignedShortLE());
        this.setDecimals(byteBuf.readByte());
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
