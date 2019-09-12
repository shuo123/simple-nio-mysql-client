package com.wws.mysqlclient.packet.connection;

import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * 切换验证方式server报文
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::AuthSwitchRequest">AuthSwitchRequest</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 11:16
 **/
@Data
public class AuthSwitchRequestPacket implements BaseSeriablizablePacket {

    /**
     * 1
     * 0xfe
     */
    private byte status = (byte)0xfe;

    /**
     * string.NUL
     */
    private String authMethodName;

    /**
     * string.EOF
     */
    private byte[] authMethodData;

    @Override
    public void read(ByteBuf byteBuf) {
        this.setStatus(byteBuf.readByte());
        this.setAuthMethodName(new String(MysqlByteBufUtil.readUtilNUL(byteBuf)));
        this.setAuthMethodData(MysqlByteBufUtil.readUtilEOF(byteBuf));
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
