package com.wws.mysqlclient.protocol.packet;

import io.netty.buffer.ByteBuf;

public interface BaseSeriablizablePacket {

    void read(ByteBuf byteBuf);

    ByteBuf write();

}
