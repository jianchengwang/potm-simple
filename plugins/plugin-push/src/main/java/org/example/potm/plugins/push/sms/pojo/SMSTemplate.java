package org.example.potm.plugins.push.sms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSTemplate {
    private String signName; // 签名
    private String templateId; // 模板编号
    private String templateName; // 模板名称
    private String templateContent; // 模板内容
}
