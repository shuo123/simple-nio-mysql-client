package com.wws.mysqlclient.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * mysql报文
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 16:42
 **/
@Data
@NoArgsConstructor
public class MysqlPacket implements BaseSeriablizablePacket {

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

    @Override
    public void read(ByteBuf byteBuf) {
        this.setPayloadLength(byteBuf.readMediumLE());
        this.setSequenceId(byteBuf.readByte());
        this.setPayload(byteBuf.readBytes(payloadLength));
    }

    @Override
    public ByteBuf write(){
        ByteBuf header = ByteBufAllocator.DEFAULT.buffer(4);
        header.writeMediumLE(payloadLength);
        header.writeByte(sequenceId);
        return Unpooled.wrappedBuffer(header, payload);
    }



}
