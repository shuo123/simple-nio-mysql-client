package com.wws.mysqlclient.packet.connection;

import lombok.Data;

/**
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::AuthMoreData">AuthMoreData</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 11:25
 **/
@Data
public class AuthMoreDataPacket {

    /**
     * 1
     * 0x01
     */
    private byte status;

    /**
     * string.EOF
     */
    private String pluginData;

}
