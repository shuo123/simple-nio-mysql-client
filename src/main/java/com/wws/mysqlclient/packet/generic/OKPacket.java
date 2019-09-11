package com.wws.mysqlclient.packet.generic;

import lombok.Data;

/**
 * ok报文
 * @see <a href="https://dev.mysql.com/doc/internals/en/packet-OK_Packet.html" >OK_Packet</a>
 *
 * OK：header= 0，数据包长度> 7
 *
 * EOF：header= 0xfe，数据包长度<9
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 10:37
 **/
@Data
public class OKPacket {

    /**
     * INT <1>
     * [00]或[fe]OK包头
     */
    private byte header;

    /**
     * INT <lenenc>
     * 受影响的行
     */
    private long affectRow;

    /**
     * INT <lenenc>
     * 最后一个插入ID
     */
    private long lastInsertId;

    /**
     * INT <2>
     * 状态标志
     */
    private short statusFlags;

    /**
     * INT <2>
     * 警告数量
     */
    private short warnings;

    /**
     * string<EOF>
     * 服务器状态信息
     */
    private String info;

}
