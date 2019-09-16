package com.wws.mysqlclient.plugin.impl;

import com.wws.mysqlclient.plugin.AuthPlugin;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 14:39
 **/
public class CachingSHA2PasswordPlugin implements AuthPlugin {

    private static final int CACHING_SHA2_DIGEST_LENGTH = 32;

    @Override
    public byte[] generate(byte[] password, byte[] seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

//            byte[] dig1 = messageDigest.digest(password);
//            messageDigest.reset();
//
//            byte[] dig2 = messageDigest.digest(dig1);
//            messageDigest.reset();
//
//            messageDigest.update(dig2);
//            messageDigest.update(seed);
//            byte[] dig3 = messageDigest.digest();
//
//            return xor(dig1, dig3);

            byte[] dig1 = new byte[CACHING_SHA2_DIGEST_LENGTH];
            byte[] dig2 = new byte[CACHING_SHA2_DIGEST_LENGTH];
            byte[] scramble1 = new byte[CACHING_SHA2_DIGEST_LENGTH];

            // SHA2(src) => digest_stage1
            md.update(password, 0, password.length);
            md.digest(dig1, 0, CACHING_SHA2_DIGEST_LENGTH);
            md.reset();

            // SHA2(digest_stage1) => digest_stage2
            md.update(dig1, 0, dig1.length);
            md.digest(dig2, 0, CACHING_SHA2_DIGEST_LENGTH);
            md.reset();

            // SHA2(digest_stage2, m_rnd) => scramble_stage1
            md.update(dig2, 0, dig1.length);
            md.update(seed, 0, seed.length);
            md.digest(scramble1, 0, CACHING_SHA2_DIGEST_LENGTH);


            return xor(dig1, scramble1);
        }catch (NoSuchAlgorithmException | DigestException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {





        byte[] seed1 = {0x58,0x58,0x10,0x2d,0x25,0x27,0x27,0x28};
        byte[] seed2 = {0x2b,0x31,0x7c,0x5a,0x5b,0x57,0x13,0x5d,0x0b,0x2c,0x5c,0x14};
        byte[] seed = new byte[seed1.length + seed2.length];
        System.arraycopy(seed1, 0, seed, 0, seed1.length);
        System.arraycopy(seed2, 0, seed, seed1.length, seed2.length);
        CachingSHA2PasswordPlugin cachingSHA2PasswordPlugin = new CachingSHA2PasswordPlugin();
        byte[] generate = cachingSHA2PasswordPlugin.generate("root".getBytes(StandardCharsets.UTF_8), seed);

        String s = dumpAsHex(generate, generate.length);
        System.out.println(s);


    }

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
