package com.wws.mysqlclient.packet.generic;

import lombok.Data;

/**
 * err报文
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html">ERR_Packet</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 10:53
 **/
@Data
public class ErrPacket {

    private byte header;

    private short errorCode;

    private String sqlStateMarker;

    private String sqlState;

    private String errorMessage;

}
