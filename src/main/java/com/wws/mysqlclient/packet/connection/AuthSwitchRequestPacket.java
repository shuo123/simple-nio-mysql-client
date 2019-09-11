package com.wws.mysqlclient.packet.connection;

import lombok.Data;

/**
 * 切换验证方式server报文
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::AuthSwitchRequest">AuthSwitchRequest</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 11:16
 **/
@Data
public class AuthSwitchRequestPacket {

    /**
     * 1
     * 0xfe
     */
    private byte status;

    /**
     * string.NUL
     */
    private String authMethodName;

    /**
     * string.EOF
     */
    private String authMethodData;

}
