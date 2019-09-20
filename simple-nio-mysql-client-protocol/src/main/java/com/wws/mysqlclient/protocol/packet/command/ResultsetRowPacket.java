package com.wws.mysqlclient.protocol.packet.command;

import com.wws.mysqlclient.protocol.packet.BaseLengthPacket;
import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.common.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-query-response.html#text-resultset-row">ResultsetRow</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-18 19:25
 **/
@Data
public class ResultsetRowPacket implements BaseSeriablizablePacket, BaseLengthPacket {

    private List<String> row = new LinkedList<>();

    @Override
    public int length() {
        return 0;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        while (byteBuf.isReadable()){
            row.add(MysqlByteBufUtil.readLengthEncodedString(byteBuf));
        }
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
