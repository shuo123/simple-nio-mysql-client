package com.wws.mysqlclient.plugin.impl;

import com.wws.mysqlclient.plugin.AuthPlugin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @see <a href="https://dev.mysql.com/doc/internals/en/secure-password-authentication.html#packet-Authentication::Native41">Native41</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 14:42
 **/
public class MysqlNativePasswordPlugin implements AuthPlugin {

    @Override
    public byte[] generate(byte[] password, byte[] seed) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            byte[] dig1 = messageDigest.digest(password);
            messageDigest.reset();

            byte[] dig2 = messageDigest.digest(dig1);
            messageDigest.reset();

            messageDigest.update(seed);
            messageDigest.update(dig2);
            byte[] dig3 = messageDigest.digest();

            return xor(dig1, dig3);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
