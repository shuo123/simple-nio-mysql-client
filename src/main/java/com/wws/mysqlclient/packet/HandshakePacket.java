package com.wws.mysqlclient.packet;

import lombok.Data;

/**
 * 握手协议
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 14:46
 **/
@Data
public class HandshakePacket {

    //1
    private byte protocolVersion;

    //string[NUL]
    private String serverVersion;

    //4
    private int connectionId;

    //8
    private byte[] authPluginDataPart1;

    //2
    private short capabilityFlagsLower;

    //1
    private byte charsetFlag;

    //2
    private short serverStatusFlag;

    //2
    private short capabilityFlagsUpper;

    //1
    private byte authPluginDataLength;

    //10
    private byte[] reserved;

    //len=MAX(13, length of auth-plugin-data - 8)
    private byte[] authPluginDataPart2;

    //string[NUL]
    private String authPluginName;
}
