package com.wws.mysqlclient.packet.connection;

import com.wws.mysqlclient.packet.BaseSeriablizablePacket;
import com.wws.mysqlclient.plugin.impl.CachingSHA2PasswordPlugin;
import com.wws.mysqlclient.util.MysqlByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.Arrays;

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
    private short status = 0xfe;

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
        this.setStatus(byteBuf.readUnsignedByte());
        this.setAuthMethodName(new String(MysqlByteBufUtil.readUtilNUL(byteBuf)));
        byte[] bytes = MysqlByteBufUtil.readUtilEOF(byteBuf);
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == (byte) 0) {
            i--;
        }
        this.setAuthMethodData(Arrays.copyOf(bytes, i + 1));
        CachingSHA2PasswordPlugin.dumpAsHex(getAuthMethodData(), getAuthMethodData().length);
    }

    @Override
    public ByteBuf write() {
        return null;
    }
}
