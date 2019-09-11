package com.wws.mysqlclient.handler;

import com.wws.mysqlclient.packet.MysqlPacket;
import com.wws.mysqlclient.packet.connection.AuthSwitchRequestPacket;
import com.wws.mysqlclient.packet.generic.ErrPacket;
import com.wws.mysqlclient.packet.generic.OKPacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
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
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, MysqlPacket mysqlPacket, List<Object> list) throws Exception {
        ByteBuf payload = mysqlPacket.getPayload();
        byte header = payload.readByte();
        if((byte)0xff == header){
            ErrPacket errPacket = new ErrPacket();
            errPacket.setErrorCode(payload.readShortLE());
            errPacket.setSqlState(new String(MysqlByteBufUtil.readNByte(payload,1)));
            errPacket.setSqlStateMarker(new String(MysqlByteBufUtil.readNByte(payload,5)));
            errPacket.setErrorMessage(new String(MysqlByteBufUtil.readUtilEOF(payload)));
            list.add(errPacket);
        }else if((byte)0 == header) {
            OKPacket okPacket = new OKPacket();
            okPacket.setHeader(header);
            okPacket.setAffectRow(MysqlByteBufUtil.readLengthEncodedInteger(payload));
            okPacket.setLastInsertId(MysqlByteBufUtil.readLengthEncodedInteger(payload));
            okPacket.setStatusFlags(payload.readShortLE());
            okPacket.setWarnings(payload.readShortLE());
            okPacket.setInfo(new String(MysqlByteBufUtil.readUtilEOF(payload)));
            list.add(okPacket);
        }else if((byte)0xfe == header){
            AuthSwitchRequestPacket authSwitchRequestPacket = new AuthSwitchRequestPacket();
            authSwitchRequestPacket.setAuthMethodName(new String(MysqlByteBufUtil.readUtilNUL(payload)));
            authSwitchRequestPacket.setAuthMethodData(new String(MysqlByteBufUtil.readUtilEOF(payload)));
            list.add(authSwitchRequestPacket);
        }else{
            System.out.println("error....,"+ payload);
        }
    }
}
