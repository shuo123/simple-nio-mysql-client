package handler;

import enums.AttributeKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.Attribute;
import packet.HandshakePacket;
import packet.MysqlPacket;

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
        HandshakePacket handshakePacket = new HandshakePacket();
        ByteBuf payload = mysqlPacket.getPayload();
        handshakePacket.setProtocolVersion(payload.readByte());
        handshakePacket.setServerVersion(new String(readUtilNul(payload)));
        handshakePacket.setConnectionId(payload.readIntLE());
        handshakePacket.setAuthPluginDataPart1(readBytes(payload, 8));
        payload.skipBytes(1);
        handshakePacket.setCapabilityFlagsLower(payload.readShortLE());
        handshakePacket.setCharsetFlag(payload.readByte());
        handshakePacket.setServerStatusFlag(payload.readShortLE());
        handshakePacket.setCapabilityFlagsUpper(payload.readShortLE());
        handshakePacket.setAuthPluginDataLength(payload.readByte());
        handshakePacket.setReserved(readBytes(payload, 10));
        handshakePacket.setAuthPluginDataPart2(readBytes(payload, Math.max(13, (int)handshakePacket.getAuthPluginDataLength() - 8)));
        handshakePacket.setAuthPluginName(new String(readUtilNul(payload)));

        Attribute<AtomicInteger> sequenceId = channelHandlerContext.channel().attr(AttributeKeys.SEQUENCE_ID_KEY);
        sequenceId.setIfAbsent(new AtomicInteger(mysqlPacket.getSequenceId()));

        System.out.println(handshakePacket);
        list.add(handshakePacket);
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
