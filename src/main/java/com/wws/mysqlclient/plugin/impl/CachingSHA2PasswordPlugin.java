package com.wws.mysqlclient.plugin.impl;

import com.wws.mysqlclient.plugin.AuthPlugin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 14:39
 **/
public class CachingSHA2PasswordPlugin implements AuthPlugin {

    @Override
    public byte[] generate(byte[] password, byte[] seed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] dig1 = messageDigest.digest(password);
            messageDigest.reset();

            byte[] dig2 = messageDigest.digest(dig1);
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
}
