package org.example.potm.plugins.push.sms.adapter;

import org.example.potm.framework.exception.ClientException;
import org.example.potm.plugins.push.sms.constant.SMSErrorCode;
import org.example.potm.plugins.push.sms.pojo.SMSObject;
import org.example.potm.plugins.push.sms.pojo.SMSTemplate;

import java.util.List;
import java.util.Optional;

public interface SMSAdapter {
    default SMSTemplate getSMSTemplate(SMSObject smsObject, List<SMSTemplate> smsTemplateList) {
        String templateId = smsObject.getTemplateId();
        Optional<SMSTemplate> smsTemplateOptional = smsTemplateList.stream().filter(t -> t.getTemplateId().equals(templateId)).findFirst();
        if(smsTemplateOptional.isEmpty()) {
            throw new ClientException(SMSErrorCode.TEMPLATE_ID_NOT_FOUND);
        }
        return smsTemplateOptional.get();
    }

    /**
     * 发送短信
     * @param smsObject
     */
    void sendSms(SMSObject smsObject);
}
