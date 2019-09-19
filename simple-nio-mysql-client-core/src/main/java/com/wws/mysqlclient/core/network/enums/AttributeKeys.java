package com.wws.mysqlclient.core.network.enums;

import com.wws.mysqlclient.core.FuturePacket;
import com.wws.mysqlclient.core.config.MysqlConfig;
import com.wws.mysqlclient.protocol.packet.command.ComQueryResponsePacket;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultPromise;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-10 15:15
 **/
public class AttributeKeys {

    public static final AttributeKey<Byte> SEQUENCE_ID_KEY = AttributeKey.newInstance("sequenceId");
    public static final AttributeKey<MysqlConfig> CONFIG_KEY = AttributeKey.newInstance("config");
    public static final AttributeKey<FuturePacket<ComQueryResponsePacket>> COM_QUERY_RESPONSE_PACKET_KEY = AttributeKey.newInstance("comQueryResponsePacket");
    public static final AttributeKey<DefaultPromise<Void>> CONNECTION = AttributeKey.newInstance("connection");
}
