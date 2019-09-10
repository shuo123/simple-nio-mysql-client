package config;

import lombok.Data;

/**
 * mysql配置
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-09 19:31
 **/
@Data
public class MysqlConfig {

    private String username;

    private String password;

    private String database;

}
