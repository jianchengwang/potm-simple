package org.example.potm.plugins.push.sms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.example.potm.plugins.push.sms.adapter.MySubMailAdapter;
import org.example.potm.plugins.push.sms.adapter.SMSAdapter;
import org.example.potm.plugins.push.sms.autoconfigure.properties.SMSProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(SMSProperties.class)
@ConditionalOnExpression("${plugins.sms.enable:false}")
public class SMSAutoConfigure {

    @Bean
    @ConditionalOnProperty(name = "plugins.sms.cloud-type", havingValue = "mysubmail")
    @ConditionalOnMissingBean
    public SMSAdapter autoConfigMySubMailAdapter(SMSProperties properties) {
        properties.getMysubmail().validate();
        return new MySubMailAdapter(properties.getMysubmail());
    }
}
