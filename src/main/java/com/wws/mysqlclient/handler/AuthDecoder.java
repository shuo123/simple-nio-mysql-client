package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.packet.MysqlPacket;
import com.wws.mysqlclient.packet.connection.AuthSwitchRequestPacket;
import com.wws.mysqlclient.packet.generic.ErrPacket;
import com.wws.mysqlclient.packet.generic.OKPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 验证结果解码器
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 14:44
 **/
public class AuthDecoder extends MessageToMessageDecoder<MysqlPacket> {

    private static final short ERR_HEADER = 0XFF;
    private static final short OK_HEADER = 0X00;
    private static final short AUTH_SWITCH_HEADER = 0XFE;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, MysqlPacket mysqlPacket, List<Object> list) {
        ByteBuf payload = mysqlPacket.getPayload();
        short header = payload.getUnsignedByte(payload.readerIndex());
        if(ERR_HEADER == header){
            ErrPacket errPacket = new ErrPacket();
            errPacket.read(payload);
            System.out.println(errPacket);
            list.add(errPacket);
        }else if(OK_HEADER == header) {
            OKPacket okPacket = new OKPacket();
            okPacket.read(payload);
            System.out.println(okPacket);
            list.add(okPacket);
        }else if(AUTH_SWITCH_HEADER == header){
            AuthSwitchRequestPacket authSwitchRequestPacket = new AuthSwitchRequestPacket();
            authSwitchRequestPacket.read(payload);
            System.out.println(authSwitchRequestPacket);
            list.add(authSwitchRequestPacket);
        }else{
            System.out.println("error....,"+ payload);
        }
    }
}
