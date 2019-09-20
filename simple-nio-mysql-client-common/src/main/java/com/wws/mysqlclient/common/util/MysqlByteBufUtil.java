package com.wws.mysqlclient.common.util;

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

    private static final int LENGTH_ENCODED_FIX_251 = 0xfb;
    private static final int LENGTH_ENCODED_FIX_252 = 0xfc;
    private static final int LENGTH_ENCODED_FIX_253 = 0xfd;
    private static final int LENGTH_ENCODED_FIX_254 = 0xfe;

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
     * @see <a href="https://dev.mysql.com/doc/internals/en/integer.html#packet-Protocol::LengthEncodedInteger">LengthEncodedInteger</a>
     *
     * @param byteBuf ByteBuf
     * @return LengthEncodedInteger
     */
    public static long readLengthEncodedInteger(ByteBuf byteBuf) {
        short b = byteBuf.readUnsignedByte();
        if (b < LENGTH_ENCODED_FIX_251) {
            return b;
        } else if (b == LENGTH_ENCODED_FIX_252) {
            return byteBuf.readShortLE();
        } else if (b == LENGTH_ENCODED_FIX_253) {
            return byteBuf.readIntLE();
        } else if (b == LENGTH_ENCODED_FIX_254) {
            return byteBuf.readLongLE();
        }

        return 0;
    }

    /**
     * 读取lengthEncodedString
     *
     * @param byteBuf ByteBuf
     * @return String
     */
    public static String readLengthEncodedString(ByteBuf byteBuf) {
        long len = readLengthEncodedInteger(byteBuf);
        if(len == 0){
            return null;
        }
        return new String(readNByte(byteBuf, (int) len), StandardCharsets.UTF_8);
    }

    /**
     * 写入lengthEncodedInteger
     *
     * @param byteBuf ByteBuf
     * @param i       long
     */
    public static void writeLengthEncodedInteger(ByteBuf byteBuf, long i) {
        if (i < LENGTH_ENCODED_FIX_251) {
            byteBuf.writeByte((byte) i);
        } else if (i < (1 << 16)) {
            byteBuf.writeByte(LENGTH_ENCODED_FIX_252);
            byteBuf.writeShortLE((short) i);
        } else if (i < (1 << 24)) {
            byteBuf.writeByte(LENGTH_ENCODED_FIX_253);
            byteBuf.writeMediumLE((int) i);
        } else {
            byteBuf.writeByte(LENGTH_ENCODED_FIX_254);
            byteBuf.writeLongLE(i);
        }
    }

    /**
     * 写入LengthEncodedString
     *
     * @param byteBuf ByteBuf
     * @param str     byte[]
     */
    public static void writeLengthEncodedString(ByteBuf byteBuf, byte[] str) {
        writeLengthEncodedInteger(byteBuf, str.length);
        byteBuf.writeBytes(str);
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
     * 写入string
     *
     * @param byteBuf ByteBuf
     * @param str     String
     */
    public static void writeString(ByteBuf byteBuf, String str) {
        byteBuf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 输出16进制
     *
     * @param byteBuffer
     * @param length
     * @return
     */
    public static String dumpAsHex(byte[] byteBuffer, int length) {
        StringBuilder outputBuilder = new StringBuilder(length * 4);

        int p = 0;
        int rows = length / 8;

        for (int i = 0; (i < rows) && (p < length); i++) {
            int ptemp = p;

            for (int j = 0; j < 8; j++) {
                String hexVal = Integer.toHexString(byteBuffer[ptemp] & 0xff);

                if (hexVal.length() == 1) {
                    hexVal = "0" + hexVal;
                }

                outputBuilder.append(hexVal + " ");
                ptemp++;
            }

            outputBuilder.append("    ");

            for (int j = 0; j < 8; j++) {
                int b = 0xff & byteBuffer[p];

                if (b > 32 && b < 127) {
                    outputBuilder.append((char) b + " ");
                } else {
                    outputBuilder.append(". ");
                }

                p++;
            }

            outputBuilder.append("\n");
        }

        int n = 0;

        for (int i = p; i < length; i++) {
            String hexVal = Integer.toHexString(byteBuffer[i] & 0xff);

            if (hexVal.length() == 1) {
                hexVal = "0" + hexVal;
            }

            outputBuilder.append(hexVal + " ");
            n++;
        }

        for (int i = n; i < 8; i++) {
            outputBuilder.append("   ");
        }

        outputBuilder.append("    ");

        for (int i = p; i < length; i++) {
            int b = 0xff & byteBuffer[i];

            if (b > 32 && b < 127) {
                outputBuilder.append((char) b + " ");
            } else {
                outputBuilder.append(". ");
            }
        }

        outputBuilder.append("\n");

        return outputBuilder.toString();
    }

}
