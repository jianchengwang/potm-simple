package org.example.potm.plugins.wxmp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedissonConfigImpl;
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

@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {

    private final WxMpProperties properties;
    private final Boolean useRedis;
    private final WxMpProperties.RedisConfig redisConfig;
    private final RedissonClient redissonClient;

    @Autowired
    public WxMpConfiguration(WxMpProperties properties, @Nullable RedissonClient redissonClient) {
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

    public RedissonClient createRedissionClient(WxMpProperties.RedisConfig redisConfig) {
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
    public WxMpService wxMpService() {
        List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new ClientException("对应的微信小程序配置不存在", FrameworkErrorCode.INVALID_CONFIG);
        }
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setMultiConfigStorages(
                configs.stream()
                        .map(a -> {
                            WxMpDefaultConfigImpl config;
                            if (redissonClient != null) {
                                config = new WxMpRedissonConfigImpl(redissonClient, a.getAppId());
                            } else {
                                config = new WxMpDefaultConfigImpl();
                            }
                            config.setAppId(a.getAppId());
                            config.setSecret(a.getSecret());
                            return config;
                        }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return wxMpService;
    }


}
