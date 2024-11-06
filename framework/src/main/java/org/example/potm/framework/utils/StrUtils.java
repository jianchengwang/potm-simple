package org.example.potm.framework.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StrUtils {

    public static Map<String, String> getParamMap(String params) {
        Map<String, String> paramMap = new ConcurrentHashMap<>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(params)) {
            String[] paramArr = params.split(";");
            for(String param: paramArr) {
                String[] kv = param.split("=");
                if(kv.length == 2) {
                    paramMap.put(kv[0], kv[1]);
                }
            }
        }
        return paramMap;
    }
}
