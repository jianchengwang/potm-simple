package org.example.potm.plugins.push.sms.adapter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.utils.JSONUtils;
import org.example.potm.plugins.push.sms.autoconfigure.properties.MySubMailProperties;
import org.example.potm.plugins.push.sms.constant.SMSErrorCode;
import org.example.potm.plugins.push.sms.pojo.SMSObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MySubMailAdapter implements SMSAdapter {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String subMailUrl;
    private final String appId;
    private final String signature;
    public MySubMailAdapter(final MySubMailProperties config) {
        this.subMailUrl = config.getUrl();
        this.appId = config.getAppId();
        this.signature = config.getSignature();
    }

    @Override
    public void sendSms(SMSObject smsObject) {
        HttpHeaders httpHeaders = getHeaders();
        Map<String, Object> map = new HashMap<>();
        map.put(CONSTANT.PARAM_appid, appId);
        map.put(CONSTANT.PARAM_signature, signature);
        map.put(CONSTANT.PARAM_to, smsObject.getPhoneNumbers());
        map.put(CONSTANT.PARAM_content, smsObject.getContent());
        try {
            ResponseBean res = restTemplatePost(subMailUrl, map, httpHeaders);
            validateResponse(res);
        } catch (Exception e) {
            throw new ClientException(SMSErrorCode.SECOND_SMS_ERROR);
        }
    }

    private static interface CONSTANT {
        String PARAM_appid = "appid";
        String PARAM_signature = "signature";
        String PARAM_to = "to";
        String PARAM_content = "content";
    }

    @Getter
    @Setter
    private static class ResponseBean {
        private String status;
        private String send_id;
        private String fee;
        private String code;
        private String msg;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        return httpHeaders;
    }

    private ResponseBean restTemplatePost(String url, Map<String, Object> params, HttpHeaders headers) {
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, ResponseBean.class).getBody();
    }

    private void validateResponse(MySubMailAdapter.ResponseBean res) {
        // 输出字符串回包
        log.info("mysubmail sms 返回：" + JSONUtils.jsonString(res));
    }
}
