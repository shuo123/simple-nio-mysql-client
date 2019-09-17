package com.wws.mysqlclient.plugin.impl;

import com.wws.mysqlclient.plugin.AuthPlugin;
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

}
