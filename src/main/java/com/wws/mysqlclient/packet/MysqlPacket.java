package com.wws.mysqlclient.packet;

import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

/**
 * mysql报文
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 16:42
 **/
@Data
@AllArgsConstructor
public class MysqlPacket {

    /**
     * 3
     */
    private Integer payloadLength;

    /**
     * 1
     */
    private byte sequenceId;

    private ByteBuf payload;

    public MysqlPacket(byte sequenceId, ByteBuf payload){
        this.sequenceId = sequenceId;
        this.payload = payload;
        this.payloadLength = payload.readableBytes();
    }

    public ByteBuf write(){
        ByteBuf header = ByteBufAllocator.DEFAULT.buffer(4);
        MysqlByteBufUtil.writePacketLen(header, payloadLength);
        header.writeByte(sequenceId);
        return Unpooled.wrappedBuffer(header, payload);
    }



}
