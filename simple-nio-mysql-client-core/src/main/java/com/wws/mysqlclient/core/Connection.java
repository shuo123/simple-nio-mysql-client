package com.wws.mysqlclient.core;

import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-19 14:31
 **/
public class Connection {

    private Channel channel;

    Connection(Channel channel){
        this.channel = channel;
    }

    public Statement createStatement(){
        return new Statement(channel);
    }

    public Future connect() {
        return channel.attr(AttributeKeys.CONNECTION).get();
    }

    public ChannelFuture close(){
        return channel.closeFuture();
    }

}
