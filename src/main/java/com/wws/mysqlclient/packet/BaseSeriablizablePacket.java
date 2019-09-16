package com.wws.mysqlclient.packet;

import io.netty.buffer.ByteBuf;

public interface BaseSeriablizablePacket {

    void read(ByteBuf byteBuf);

    ByteBuf write();

}
