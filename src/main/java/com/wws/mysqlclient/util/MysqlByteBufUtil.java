package com.wws.mysqlclient.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 15:01
 **/
public class MysqlByteBufUtil {

    /**
     * 读取n个byte
     *
     * @param byteBuf ByteBuf
     * @param n       int
     * @return byte[]
     */
    public static byte[] readNByte(ByteBuf byteBuf, int n) {
        byte[] bytes = new byte[n];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    /**
     * 读取剩余所有字节
     *
     * @param byteBuf ByteBuf
     * @return byte[]
     */
    public static byte[] readUtilEOF(ByteBuf byteBuf) {
        return readNByte(byteBuf, byteBuf.readableBytes());
    }

    /**
     * 读取到字节为NUL
     *
     * @param byteBuf ByteBuf
     * @return byte[]
     */
    public static byte[] readUtilNUL(ByteBuf byteBuf) {
        return readUtilByte(byteBuf, (byte) 0);
    }

    /**
     * 读取到字节为b
     *
     * @param byteBuf ByteBuf
     * @param b       byte
     * @return byte[]
     */
    public static byte[] readUtilByte(ByteBuf byteBuf, byte b) {
        List<Byte> list = new ArrayList<>();
        byte b1;
        while ((b1 = byteBuf.readByte()) != b) {
            list.add(b1);
        }
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    /**
     * 读取LengthEncodedInteger
     *
     * @param byteBuf ByteBuf
     * @return LengthEncodedInteger
     * @see <a href="https://dev.mysql.com/doc/internals/en/integer.html#packet-Protocol::LengthEncodedInteger">LengthEncodedInteger</a>
     */
    public static long readLengthEncodedInteger(ByteBuf byteBuf) {
        byte b = byteBuf.readByte();
        if (b < 251) {
            return b;
        } else if (b == 252) {
            return byteBuf.readShortLE();
        } else if (b == 253) {
            return byteBuf.readIntLE();
        } else if (b == 254) {
            return byteBuf.readLongLE();
        }

        return 0;
    }

    /**
     * 写入string.NUL
     *
     * @param byteBuf ByteBuf
     * @param str     String
     */
    public static void writeStringNUL(ByteBuf byteBuf, String str) {
        byteBuf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
        byteBuf.writeByte((byte) 0);
    }

    /**
     * 写入string.NUL
     *
     * @param byteBuf ByteBuf
     * @param bytes   byte[]
     */
    public static void writeStringNUL(ByteBuf byteBuf, byte[] bytes) {
        byteBuf.writeBytes(bytes);
        byteBuf.writeByte((byte) 0);
    }

    /**
     * 写入头部的报文长度
     *
     * @param byteBuf ByteBuf
     * @param packetLen 报文长度
     */
    public static void writePacketLen(ByteBuf byteBuf, int packetLen){
        byte b1 = (byte)(packetLen >> 16);
        byte b2 = (byte)((packetLen & 0xff00) >> 8);
        byte b3 = (byte)(packetLen & 0xff);
        byteBuf.writeByte(b3);
        byteBuf.writeByte(b2);
        byteBuf.writeByte(b1);
    }

}
