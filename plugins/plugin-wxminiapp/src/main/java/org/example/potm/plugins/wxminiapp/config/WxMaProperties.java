package org.example.potm.plugins.wxminiapp.config;

import lombok.Data;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jianchengwang
 * @date 2024/5/3
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private boolean useRedis = false;
    private RedisConfig redisConfig;

    private List<Config> configs;

    @Data
    public static class RedisConfig {
        /**
         * redis服务器 主机地址
         */
        private String host;

        /**
         * redis服务器 端口号
         */
        private Integer port;

        /**
         * redis服务器 密码
         */
        private String password;

        /**
         * redis 服务连接库
         */
        private Integer database;

        /**
         * redis 服务连接超时时间
         */
        private Integer timeout;
    }

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }
}
