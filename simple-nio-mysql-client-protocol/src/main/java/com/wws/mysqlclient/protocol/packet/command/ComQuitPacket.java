package com.wws.mysqlclient.protocol.packet.command;

import com.wws.mysqlclient.protocol.packet.BaseLengthPacket;
import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 断开连接报文
 * @see <a href="https://dev.mysql.com/doc/internals/en/com-quit.html">COM_QUIT</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-17 15:45
 **/
public class ComQuitPacket implements BaseLengthPacket, BaseSeriablizablePacket {

    private byte commandId = 0x01;

    @Override
    public int length() {
        return 1;
    }

    @Override
    public void read(ByteBuf byteBuf) {

    }

    @Override
    public ByteBuf write() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length());
        byteBuf.writeByte(commandId);
        return byteBuf;
    }
}
