package org.example.potm.plugins.push.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSObject {
    private String templateId; // 短信模板编号
    private String templateParamJSON; // 短信模板参数，适用于阿里云k,v参数，${code}
    private String[] templateParams; // 短信模板参数，适用于腾讯云数字占位符，{1}
    private Set<String> phoneNumbers; // 手机号码列表
    private String content; // 短信内容
}
