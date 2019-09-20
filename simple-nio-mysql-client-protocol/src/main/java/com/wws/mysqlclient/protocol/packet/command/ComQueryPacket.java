package com.wws.mysqlclient.protocol.packet.command;

import com.wws.mysqlclient.protocol.packet.BaseLengthPacket;
import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.common.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;

/**
 * 查询命令报文
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-query.html">COM_QUERY</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-17 15:35
 **/
@Data
public class ComQueryPacket implements BaseSeriablizablePacket, BaseLengthPacket {

    private byte commandId = 0x03;

    private String sql;


    @Override
    public void read(ByteBuf byteBuf) {

    }

    @Override
    public ByteBuf write() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length());
        byteBuf.writeByte(commandId);
        MysqlByteBufUtil.writeString(byteBuf, sql);
        return byteBuf;
    }

    @Override
    public int length() {
        return sql.length()+1;
    }
}
