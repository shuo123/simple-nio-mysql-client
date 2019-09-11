package com.wws.mysqlclient.packet.connection;

import lombok.Data;

/**
 * 切换验证方式client报文
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::AuthSwitchResponse">AuthSwitchResponse</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 11:22
 **/
@Data
public class AuthSwitchResponsePacket {

    /**
     * string.EOF
     */
    private String authPluginResponse;

}
