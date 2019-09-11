package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.enums.AttributeKeys;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.Attribute;
import com.wws.mysqlclient.packet.connection.HandshakeV10Packet;
import com.wws.mysqlclient.packet.MysqlPacket;

import java.util.List;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 17:02
 **/
public class HandshakeDecoder extends MessageToMessageDecoder<MysqlPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, MysqlPacket mysqlPacket, List<Object> list) {
        HandshakeV10Packet handshakeV10Packet = new HandshakeV10Packet();
        ByteBuf payload = mysqlPacket.getPayload();
        handshakeV10Packet.setProtocolVersion(payload.readByte());
        handshakeV10Packet.setServerVersion(new String(MysqlByteBufUtil.readUtilNUL(payload)));
        handshakeV10Packet.setConnectionId(payload.readIntLE());
        handshakeV10Packet.setAuthPluginDataPart1(MysqlByteBufUtil.readNByte(payload, 8));
        payload.skipBytes(1);
        handshakeV10Packet.setCapabilityFlagsLower(payload.readShortLE());
        handshakeV10Packet.setCharsetFlag(payload.readByte());
        handshakeV10Packet.setServerStatusFlag(payload.readShortLE());
        handshakeV10Packet.setCapabilityFlagsUpper(payload.readShortLE());
        handshakeV10Packet.setAuthPluginDataLength(payload.readByte());
        handshakeV10Packet.setReserved(MysqlByteBufUtil.readNByte(payload, 10));
        handshakeV10Packet.setAuthPluginDataPart2(MysqlByteBufUtil.readNByte(payload, Math.max(13, (int) handshakeV10Packet.getAuthPluginDataLength() - 8)));
        handshakeV10Packet.setAuthPluginName(new String(MysqlByteBufUtil.readUtilNUL(payload)));

        Attribute<Byte> sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY);
        sequenceId.setIfAbsent(mysqlPacket.getSequenceId());

        System.out.println(handshakeV10Packet);
        list.add(handshakeV10Packet);
    }

}
