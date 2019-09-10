package com.wws.mysqlclient.packet;

import com.wws.mysqlclient.config.MysqlConfig;
import com.wws.mysqlclient.enums.CapabilityFlags;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 握手响应协议
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeResponse /">
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 19:22
 **/
@Data
public class HandshakeResponsePacket {
    //4
    private int capabilityFlags;
    //4
    private int maxPacketSize;
    //1
    private byte charset;
    //23
    private byte[] reserved;
    //string[NUL]
    private String username;
    //string[NUL]
    private byte[] authResponse;
    //string[NUL] capabilities & CLIENT_CONNECT_WITH_DB
    private String database;
    //string[NUL] capabilities & CLIENT_PLUGIN_AUTH
    private String authPluginName;

    public static ByteBuf login(MysqlConfig config, HandshakePacket handshakePacket) throws NoSuchAlgorithmException {
        return write(getHandshakeResponsePacket(config, handshakePacket));
    }

    private static ByteBuf write(HandshakeResponsePacket handshakeResponsePacket){
        ByteBuffer byteBuffer = ByteBuffer.allocate(handshakeResponsePacket.length());
        ByteBuf byteBuf = Unpooled.wrappedBuffer(byteBuffer);
        byteBuf.writerIndex(0);
        byteBuf.writeIntLE(handshakeResponsePacket.getCapabilityFlags());
        byteBuf.writeIntLE(handshakeResponsePacket.getMaxPacketSize());
        byteBuf.writeByte(handshakeResponsePacket.getCharset());
        byteBuf.writeBytes(handshakeResponsePacket.getReserved());
        byteBuf.writeBytes(handshakeResponsePacket.getUsername().getBytes());
        byteBuf.writeByte((byte)0);
        byteBuf.writeBytes(handshakeResponsePacket.getAuthResponse());
        byteBuf.writeByte((byte)0);
        byteBuf.writeBytes(handshakeResponsePacket.getDatabase().getBytes());
        byteBuf.writeByte((byte)0);
        byteBuf.writeBytes(handshakeResponsePacket.getAuthPluginName().getBytes());
        byteBuf.writeByte((byte)0);
        return byteBuf;
    }

    private static HandshakeResponsePacket getHandshakeResponsePacket(MysqlConfig config, HandshakePacket handshakePacket) throws NoSuchAlgorithmException {
        String username = config.getUsername();
        String password = config.getPassword();
        String database = config.getDatabase();

        HandshakeResponsePacket handshakeResponsePacket = new HandshakeResponsePacket();
        int capabilityFlags = CapabilityFlags.CLIENT_LONG_PASSWORD
                | CapabilityFlags.CLIENT_FOUND_ROWS
                | CapabilityFlags.CLIENT_LONG_FLAG
                | CapabilityFlags.CLIENT_CONNECT_WITH_DB
                | CapabilityFlags.CLIENT_IGNORE_SPACE
                | CapabilityFlags.CLIENT_MULTI_STATEMENTS
                | CapabilityFlags.CLIENT_MULTI_RESULTS
                | CapabilityFlags.CLIENT_PS_MULTI_RESULTS
                | CapabilityFlags.CLIENT_PLUGIN_AUTH;

        byte[] authPluginDataPart1 = handshakePacket.getAuthPluginDataPart1();
        byte[] authPluginDataPart2 = handshakePacket.getAuthPluginDataPart2();
        byte[] seed = new byte[authPluginDataPart1.length + authPluginDataPart2.length];
        System.arraycopy(authPluginDataPart1, 0, seed, 0, authPluginDataPart1.length);
        System.arraycopy(authPluginDataPart2, 0, seed, authPluginDataPart1.length, authPluginDataPart2.length);

        handshakeResponsePacket.setCapabilityFlags(capabilityFlags);
        handshakeResponsePacket.setMaxPacketSize(-1);
        handshakeResponsePacket.setCharset((byte)255);
        handshakeResponsePacket.setReserved(new byte[23]);
        handshakeResponsePacket.setUsername(username);
        handshakeResponsePacket.setAuthResponse(cachingSHA2Password(password.getBytes(), seed));
        handshakeResponsePacket.setDatabase(database);
        handshakeResponsePacket.setAuthPluginName("caching_sha2_password");

        System.out.println(handshakeResponsePacket);
        return handshakeResponsePacket;
    }

    private static byte[] cachingSHA2Password(byte[] password, byte[] seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(password);
        byte[] dig1 = messageDigest.digest();
        messageDigest.reset();

        messageDigest.update(dig1);
        byte[] dig2 = messageDigest.digest();
        messageDigest.reset();

        messageDigest.update(dig2);
        messageDigest.update(seed);
        byte[] dig3 = messageDigest.digest();

        for(int i = 0; i < 32; i++){
            dig1[i] ^= dig3[i];
        }
        return dig1;
    }

    private int length(){
        int len = 4 + 4 + 1 + 23;
        len += username.length() + 1;
        len += authResponse.length + 1;
        len += database.length() + 1;
        len += authPluginName.length() + 1;
        return len;
    }
}
