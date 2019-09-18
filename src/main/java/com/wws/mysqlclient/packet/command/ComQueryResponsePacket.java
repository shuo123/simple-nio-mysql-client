package com.wws.mysqlclient.packet.command;

import com.wws.mysqlclient.packet.BaseLengthPacket;
import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.List;

/**
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-query-response.html">COM_QUERY Response</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-18 19:49
 **/
@Data
public class ComQueryResponsePacket implements BaseSeriablizablePacket, BaseLengthPacket {

    private long filedNumber;

    private List<ColumnDefinition41Packet> columnDefinition41PacketList;

    private List<ResultsetRowPacket> resultsetRowPacketList;

    @Override
    public int length() {
        return 1;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.setFiledNumber(MysqlByteBufUtil.readLengthEncodedInteger(byteBuf));
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
