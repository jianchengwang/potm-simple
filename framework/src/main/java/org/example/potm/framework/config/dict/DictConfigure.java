package org.example.potm.framework.config.dict;

import org.example.potm.framework.config.redis.RedisCacheConfigure;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Configuration
@AutoConfigureAfter({
        RedisCacheConfigure.class,
        JdbcTemplateAutoConfiguration.class
})
@EnableConfigurationProperties(DictProperties.class)
public class DictConfigure {

    @Value("${spring.application.name:default}")
    private String svcName;

    @Bean
    @ConditionalOnMissingBean
    public DictProcessor dictEnumSyncDbProcessor(
            JdbcTemplate jdbcTemplate,
            DictProperties dictProperties,
            ObjectProvider<DictProvider> objectDictProvider) {
        return new DictProcessor(svcName, dictProperties, jdbcTemplate, objectDictProvider);
    }

    @Bean
    public DictRedisOperator dictRedisOperator(RedissonClient redissonClient) {
        return new DictRedisOperator(redissonClient);
    }

    @Bean
    public DictUtils dictUtils(ObjectProvider<DictProvider> objectDictProvider) {
        return new DictUtils(objectDictProvider);
    }
}
