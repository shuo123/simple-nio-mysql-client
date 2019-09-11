package com.wws.mysqlclient.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 17:34
 **/
public class MysqlAuthUtil {

    public static byte[] cachingSHA2Password(byte[] password, byte[] seed) {
        try {
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

            return xor(dig1, dig3);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] nativePassword(byte[] password, byte[] seed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            messageDigest.update(password);
            byte[] dig1 = messageDigest.digest();
            messageDigest.reset();

            messageDigest.update(dig1);
            byte[] dig2 = messageDigest.digest();
            messageDigest.reset();

            byte[] bytes = new byte[seed.length + password.length];
            System.arraycopy(seed, 0, bytes, 0, seed.length);
            System.arraycopy(dig2, 0, bytes, seed.length, dig2.length);
            messageDigest.update(bytes);
            byte[] dig3 = messageDigest.digest();

            return xor(dig1, dig3);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] xor(byte[] bytes1, byte[] bytes2){
        for(int i = 0; i < bytes1.length; i++){
            bytes1[i] ^= bytes2[i];
        }
        return bytes1;
    }

}
