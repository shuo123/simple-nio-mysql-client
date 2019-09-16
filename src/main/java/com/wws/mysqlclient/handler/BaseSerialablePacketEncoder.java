package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.enums.AttributeKeys;
import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.packet.MysqlPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 基础编码器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 15:42
 **/
public class BaseSerialablePacketEncoder extends MessageToMessageEncoder<BaseSeriablizablePacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseSeriablizablePacket baseSeriablizablePacket, List<Object> list) throws Exception {
        Byte sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY).get();
        sequenceId = (byte)(sequenceId + 1);

        ByteBuf payload = baseSeriablizablePacket.write();
        MysqlPacket mysqlPacket = new MysqlPacket(sequenceId, payload);

        list.add(mysqlPacket);
    }
}
