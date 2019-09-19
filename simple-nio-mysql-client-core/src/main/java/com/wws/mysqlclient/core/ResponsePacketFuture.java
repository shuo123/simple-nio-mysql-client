package com.wws.mysqlclient.core;

import com.wws.mysqlclient.protocol.packet.BaseSeriablizablePacket;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 15:41
 **/
public class ResponsePacketFuture<T extends BaseSeriablizablePacket> implements Future<T> {

    private final Channel channel;

    private final FuturePacket<T> futurePacket;

    public ResponsePacketFuture(Channel channel, FuturePacket<T> futurePacket) {
        this.channel = channel;
        this.futurePacket = futurePacket;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return futurePacket.getCountDownLatch().getCount() == 0;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        futurePacket.getCountDownLatch().await();
        return futurePacket.getPacket();

    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        futurePacket.getCountDownLatch().await(timeout, unit);
        return futurePacket.getPacket();
    }
}
