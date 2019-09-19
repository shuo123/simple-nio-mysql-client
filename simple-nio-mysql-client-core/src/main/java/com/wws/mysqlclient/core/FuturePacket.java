package com.wws.mysqlclient.core;

import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import lombok.Data;

import java.util.concurrent.CountDownLatch;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 15:44
 **/
@Data
public class FuturePacket<T extends BaseSeriablizablePacket> {

    private T packet;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

}
