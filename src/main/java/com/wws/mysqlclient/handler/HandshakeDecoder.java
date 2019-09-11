package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.enums.AttributeKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.Attribute;
import com.wws.mysqlclient.packet.connection.HandshakeV10Packet;
import com.wws.mysqlclient.packet.MysqlPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 17:02
 **/
public class HandshakeDecoder extends MessageToMessageDecoder<MysqlPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, MysqlPacket mysqlPacket, List<Object> list) throws Exception {
        HandshakeV10Packet handshakeV10Packet = new HandshakeV10Packet();
        ByteBuf payload = mysqlPacket.getPayload();
        handshakeV10Packet.setProtocolVersion(payload.readByte());
        handshakeV10Packet.setServerVersion(new String(readUtilNul(payload)));
        handshakeV10Packet.setConnectionId(payload.readIntLE());
        handshakeV10Packet.setAuthPluginDataPart1(readBytes(payload, 8));
        payload.skipBytes(1);
        handshakeV10Packet.setCapabilityFlagsLower(payload.readShortLE());
        handshakeV10Packet.setCharsetFlag(payload.readByte());
        handshakeV10Packet.setServerStatusFlag(payload.readShortLE());
        handshakeV10Packet.setCapabilityFlagsUpper(payload.readShortLE());
        handshakeV10Packet.setAuthPluginDataLength(payload.readByte());
        handshakeV10Packet.setReserved(readBytes(payload, 10));
        handshakeV10Packet.setAuthPluginDataPart2(readBytes(payload, Math.max(13, (int) handshakeV10Packet.getAuthPluginDataLength() - 8)));
        handshakeV10Packet.setAuthPluginName(new String(readUtilNul(payload)));

        Attribute<AtomicInteger> sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY);
        sequenceId.setIfAbsent(new AtomicInteger(mysqlPacket.getSequenceId()));

        System.out.println(handshakeV10Packet);
        list.add(handshakeV10Packet);
    }

    private byte[] readUtilNul(ByteBuf byteBuf) {
        List<Byte> list = new ArrayList<>();
        byte b;
        while ((b = byteBuf.readByte()) != (byte) 0) {
            list.add(b);
        }
        byte[] bytes = new byte[list.size()];
        for(int i = 0; i < list.size(); i++){
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    private byte[] readBytes(ByteBuf byteBuf, int len){
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
