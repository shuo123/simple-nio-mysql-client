package com.wws.mysqlclient.common.plugin;

/**
 * 验证插件
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 14:36
 **/
public interface AuthPlugin {

    byte[] generate(byte[] password, byte[] seed);

    default byte[] xor(byte[] bytes1, byte[] bytes2){
        for(int i = 0; i < bytes1.length; i++){
            bytes1[i] = (byte)(bytes1[i]^bytes2[i]);
        }
        return bytes1;
    }

}
