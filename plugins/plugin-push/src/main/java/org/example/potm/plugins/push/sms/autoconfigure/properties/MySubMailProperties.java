package org.example.potm.plugins.push.sms.autoconfigure.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.plugins.push.sms.constant.SMSErrorCode;

@Data
public class MySubMailProperties {
    private String url;
    private String appId;
    private String signature;

    public void validate() {
        if(StrUtil.isEmpty(url) || StrUtil.isEmpty(appId) || StrUtil.isEmpty(signature)) {
            throw new ClientException(SMSErrorCode.CONFIG_ERROR);
        }
    }
}
