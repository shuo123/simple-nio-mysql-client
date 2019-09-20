package com.wws.mysqlclient.core.network.handler.command;

import com.wws.mysqlclient.core.FuturePacket;
import com.wws.mysqlclient.core.network.enums.AttributeKeys;
import com.wws.mysqlclient.protocol.packet.MysqlPacket;
import com.wws.mysqlclient.protocol.packet.command.ColumnDefinition41Packet;
import com.wws.mysqlclient.protocol.packet.command.ComQueryResponsePacket;
import com.wws.mysqlclient.protocol.packet.command.ResultsetRowPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-18 19:24
 **/
public class ComQueryResponseDecoder extends SimpleChannelInboundHandler<MysqlPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MysqlPacket msg) throws Exception {
        FuturePacket<ComQueryResponsePacket> futurePacket = ctx.channel().attr(AttributeKeys.COM_QUERY_RESPONSE_PACKET_KEY).get();
        ComQueryResponsePacket comQueryResponsePacket = futurePacket.getPacket();
        if(comQueryResponsePacket == null){
            comQueryResponsePacket = new ComQueryResponsePacket();
            futurePacket.setPacket(comQueryResponsePacket);
            comQueryResponsePacket.read(msg.getPayload());
        }else{
            if(!comQueryResponsePacket.isFileReadOver()){
                ColumnDefinition41Packet packet = new ColumnDefinition41Packet();
                packet.read(msg.getPayload());
                comQueryResponsePacket.getColumnDefinition41PacketList().add(packet);
                if(comQueryResponsePacket.getColumnDefinition41PacketList().size() == comQueryResponsePacket.getFiledNumber()) {
                    comQueryResponsePacket.setFileReadOver(true);
                }
            }else{
                ByteBuf payload = msg.getPayload();
                short header = payload.getUnsignedByte(payload.readerIndex());
                if(header == 0xfe){
                    comQueryResponsePacket.setRowReadOver(true);
                    futurePacket.getCountDownLatch().countDown();
                    ctx.pipeline().remove(this);
                }else {
                    ResultsetRowPacket packet = new ResultsetRowPacket();
                    packet.read(payload);
                    comQueryResponsePacket.getResultsetRowPacketList().add(packet);
                }
            }
        }
    }
}
