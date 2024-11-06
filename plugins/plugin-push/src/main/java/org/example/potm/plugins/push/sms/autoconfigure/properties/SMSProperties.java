package org.example.potm.plugins.push.sms.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties("plugins.sms")
@PropertySource(value = {"classpath:application-${spring.profiles.active}.yml"}, encoding = "UTF-8")
public class SMSProperties {
    private boolean enable = true; // 是否启用
    private CloudType cloudType = CloudType.mysubmail; // 短信提供商，目前支持mysubmail
    @NestedConfigurationProperty
    private MySubMailProperties mysubmail;
}
