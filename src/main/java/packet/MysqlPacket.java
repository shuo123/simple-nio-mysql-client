package packet;

import io.netty.buffer.ByteBuf;
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

    private Integer payloadLength;

    private byte sequenceId;

    private ByteBuf payload;

    public MysqlPacket(byte sequenceId, ByteBuf payload){
        this.sequenceId = sequenceId;
        this.payload = payload;
        this.payloadLength = payload.readableBytes();
    }

    public ByteBuf write(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        ByteBuf header = Unpooled.wrappedBuffer(byteBuffer);
        header.writerIndex(0);
        header.writeIntLE(payloadLength);
        header.writeByte(sequenceId);
        return Unpooled.wrappedBuffer(header, payload);
    }

}
