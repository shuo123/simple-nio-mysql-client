package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.enums.AttributeKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.wws.mysqlclient.packet.MysqlPacket;
import io.netty.util.Attribute;

import java.util.List;

/**
 * 握手协议解码器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 15:06
 **/
public class MysqlPacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(byteBuf);
        MysqlPacket mysqlPacket = new MysqlPacket();
        mysqlPacket.read(byteBuf);
        list.add(mysqlPacket);

        Attribute<Byte> sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY);
        sequenceId.set(mysqlPacket.getSequenceId());
    }
}
