package com.wws.mysqlclient.protocol.packet;

public interface BaseLengthPacket {

    /**
     * 获取报文长度
     * @return int
     */
    int length();

}
