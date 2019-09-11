package com.wws.mysqlclient.packet.connection;

import lombok.Data;

/**
 * 握手协议
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake">Handshake</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 14:46
 **/
@Data
public class HandshakeV10Packet {

    /**
     * 1
     */
    private byte protocolVersion;

    /**
     * string[NUL]
     */
    private String serverVersion;

    /**
     * 4
     */
    private int connectionId;

    /**
     * 8
     */
    private byte[] authPluginDataPart1;

    /**
     * 2
     */
    private short capabilityFlagsLower;

    /**
     * 1
     */
    private byte charsetFlag;

    /**
     * 2
     */
    private short serverStatusFlag;

    /**
     * 2
     */
    private short capabilityFlagsUpper;

    /**
     * 1
     */
    private byte authPluginDataLength;

    /**
     * 10
     */
    private byte[] reserved;

    /**
     * len=MAX(13, length of auth-plugin-data - 8)
     */
    private byte[] authPluginDataPart2;

    /**
     * string[NUL]
     */
    private String authPluginName;
}
