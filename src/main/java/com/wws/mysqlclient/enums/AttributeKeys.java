package com.wws.mysqlclient.enums;

import com.wws.mysqlclient.config.MysqlConfig;
import io.netty.util.AttributeKey;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-10 15:15
 **/
public class AttributeKeys {

    public static final AttributeKey<Byte> SEQUENCE_ID_KEY = AttributeKey.newInstance("sequenceId");
    public static final AttributeKey<MysqlConfig> CONFIG_KEY = AttributeKey.newInstance("config");

}
