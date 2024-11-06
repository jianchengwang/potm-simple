package org.example.potm.plugins.storage.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.example.potm.plugins.storage.adapter.FSAdapter;
import org.example.potm.plugins.storage.adapter.StorageAdapter;
import org.example.potm.plugins.storage.autoconfigure.properties.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@ConditionalOnExpression("${plugins.storage.enable:true}")
public class StorageAutoConfigure {
    /**
     * 自动配置FS存储适配器
     */
    @Bean
    @ConditionalOnProperty(name = "plugins.storage.store-type", havingValue = "fs")
    @ConditionalOnMissingBean
    public StorageAdapter autoConfigFSAdapter(StorageProperties properties) {
        properties.getFs().validate();
        return new FSAdapter(properties.getFs());
    }
}
