package com.wws.mysqlclient.core;

import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.protocol.packet.command.ComQuitPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.*;

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
        ComQuitPacket comQuitPacket = new ComQuitPacket();
        channel.attr(AttributeKeys.SEQUENCE_ID_KEY).set((byte) -1);
        ChannelFuture channelFuture = channel.writeAndFlush(comQuitPacket);
        channelFuture.addListener(future -> channel.close().sync());
        return channelFuture;
    }

    private EventExecutor getEventExecutor(){
        return channel.attr(AttributeKeys.EVENTEXECUTOR).get();
    }

}
