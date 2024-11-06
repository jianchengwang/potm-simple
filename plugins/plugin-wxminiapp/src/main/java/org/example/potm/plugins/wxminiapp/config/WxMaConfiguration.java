package org.example.potm.plugins.wxminiapp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.utils.JSONUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianchengwang
 * @date 2024/5/3
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfiguration {
    private final WxMaProperties properties;
    private final Boolean useRedis;
    private final WxMaProperties.RedisConfig redisConfig;
    private final RedissonClient redissonClient;

    @Autowired
    public WxMaConfiguration(WxMaProperties properties, @Nonnull RedissonClient redissonClient) {
        this.properties = properties;
        this.useRedis = properties.isUseRedis();
        redisConfig = properties.getRedisConfig();
        if(useRedis) {
            if(redisConfig != null) {
                this.redissonClient = createRedissionClient(redisConfig);
            } else {
                this.redissonClient = redissonClient;
            }
        } else {
            this.redissonClient = null;
        }
    }

    public RedissonClient createRedissionClient(WxMaProperties.RedisConfig redisConfig) {
        Config conf = new Config();
        //单节点模式
        SingleServerConfig singleServerConfig = conf.useSingleServer();
        //设置连接地址：redis://127.0.0.1:6379
        singleServerConfig.setAddress(String.format("redis://%s:%s", redisConfig.getHost(), redisConfig.getPort()));
        //设置连接密码
        if(StringUtils.isNotEmpty(redisConfig.getPassword())) {
            singleServerConfig.setPassword(redisConfig.getPassword());
        }
        // 设置连接库
        singleServerConfig.setDatabase(redisConfig.getDatabase());
        singleServerConfig.setConnectionPoolSize(5);
        singleServerConfig.setConnectionMinimumIdleSize(2);
        //使用json序列化方式
        ObjectMapper objectMapper = new ObjectMapper();
        JSONUtils.buildObjectMapper(objectMapper, true);
        Codec codec = new JsonJacksonCodec(objectMapper);
        conf.setCodec(codec);
        return Redisson.create(conf);
    }

    @Bean
    public WxMaService wxMaService() {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new ClientException("对应的微信小程序配置不存在", FrameworkErrorCode.INVALID_CONFIG);
        }
        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(
                configs.stream()
                        .map(a -> {
                            WxMaDefaultConfigImpl config;
                            if(redissonClient != null) {
                                config = new WxMaRedissonConfigImpl(redissonClient);
                            } else {
                                config = new WxMaDefaultConfigImpl();
                            }
                            config.setAppid(a.getAppid());
                            config.setSecret(a.getSecret());
                            config.setToken(a.getToken());
                            config.setAesKey(a.getAesKey());
                            config.setMsgDataFormat(a.getMsgDataFormat());
                            return config;
                        }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        return maService;
    }

}
