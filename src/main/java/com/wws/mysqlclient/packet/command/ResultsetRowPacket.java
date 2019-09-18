package com.wws.mysqlclient.packet.command;

import com.wws.mysqlclient.packet.BaseLengthPacket;
import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

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

    private short header = 0xfe;

    private List<String> row;

    @Override
    public int length() {
        return 0;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        byteBuf.skipBytes(1);
        while (byteBuf.isReadable()){
            MysqlByteBufUtil.readLengthEncodedInteger(byteBuf);
        }
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
