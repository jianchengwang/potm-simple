package org.example.potm.plugins.storage.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties("plugins.storage")
@PropertySource(value = {"classpath:application-${spring.profiles.active}.yml"}, encoding = "UTF-8")
public class StorageProperties {
    private boolean enable = true; // 是否启用
    private StoreType storeType = StoreType.fs; // 存储类型，目前支持fs
    @NestedConfigurationProperty
    private FSProperties fs;
}
