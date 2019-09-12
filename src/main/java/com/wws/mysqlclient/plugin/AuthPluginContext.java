package com.wws.mysqlclient.plugin;

import com.wws.mysqlclient.plugin.impl.CachingSHA2PasswordPlugin;
import com.wws.mysqlclient.plugin.impl.MysqlNativePasswordPlugin;

/**
 * @author wws
 * @version 1.0.0
 * @date 2019-09-12 14:43
 **/
public enum AuthPluginContext {
    /**
     * 旧版默认验证插件
     */
    mysql_native_password(new MysqlNativePasswordPlugin()),
    /**
     * 新版默认验证插件
     */
    caching_sha2_password(new CachingSHA2PasswordPlugin());


    private AuthPlugin authPlugin;

    AuthPluginContext(AuthPlugin authPlugin) {
        this.authPlugin = authPlugin;
    }

    private byte[] doGenerate(byte[] password, byte[] seed){
        return authPlugin.generate(password, seed);
    }

    public static byte[] generate(String name, byte[] password, byte[] seed){
        AuthPluginContext authPluginContext = valueOf(name);
        return authPluginContext.doGenerate(password, seed);
    }

}
