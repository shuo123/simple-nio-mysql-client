package com.wws.mysqlclient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.wws.mysqlclient.packet.MysqlPacket;

import java.util.List;

/**
 * 握手协议解码器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 15:06
 **/
public class MysqlPacketDecoder extends ByteToMessageDecoder {

    private final int HEADER_LEN = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() > HEADER_LEN){
            byte[] bytes = new byte[HEADER_LEN];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            int payloadLen = getPayloadLen(bytes);
            if(byteBuf.readableBytes() >= HEADER_LEN + payloadLen){
                byteBuf.skipBytes(4);
                MysqlPacket mysqlPacket = new MysqlPacket(payloadLen, bytes[3], byteBuf.readBytes(payloadLen));
                list.add(mysqlPacket);
            }
        }
    }

    private int getPayloadLen(byte[] bytes){
        int len = bytes[0];
        len += bytes[1] << 8;
        len += bytes[2] << 16;
        return len;
    }
}
