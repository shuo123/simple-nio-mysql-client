package com.wws.mysqlclient.enums;

import com.wws.mysqlclient.config.MysqlConfig;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-10 15:15
 **/
public class AttributeKeys {

    public static final AttributeKey<AtomicInteger> SEQUENCE_ID_KEY = AttributeKey.newInstance("sequenceId");
    public static final AttributeKey<AtomicInteger> STATUS_KEY = AttributeKey.newInstance("status");
    public static final AttributeKey<MysqlConfig> CONFIG_KEY = AttributeKey.newInstance("com/wws/mysqlclient/config");

}
