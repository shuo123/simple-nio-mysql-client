package com.wws.mysqlclient.core.network.handler;

import com.wws.mysqlclient.protocol.packet.MysqlPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 15:46
 **/
public class MysqlPacketEecoder extends MessageToByteEncoder<MysqlPacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MysqlPacket mysqlPacket, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(mysqlPacket.write());
    }
}
