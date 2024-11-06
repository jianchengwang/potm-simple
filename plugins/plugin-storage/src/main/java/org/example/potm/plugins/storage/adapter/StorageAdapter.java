package org.example.potm.plugins.storage.adapter;

import cn.hutool.core.util.StrUtil;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.exception.ServerException;
import org.example.potm.plugins.storage.autoconfigure.properties.StoreType;
import org.example.potm.plugins.storage.pojo.PutObject;
import org.example.potm.plugins.storage.pojo.SObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

public interface StorageAdapter {

    default String encodeKey(String key) {
        try {
            return URLEncoder.encode(key, "utf-8").replace("+", "%20");
        } catch (Exception e) {
            throw new ServerException("key encode error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    default void encodeKey(List<String> keyList) {
        keyList.forEach(key -> {
            key = encodeKey(key);
        });
    }

    StoreType getStoreType();

    SObject doPut(String key, InputStream inputStream, boolean forcedPut);

    default List<SObject> doBatchPut(List<PutObject> objectList, boolean forcedPut) {
        return objectList.stream().map(object -> {
                    SObject sObject = doPut(object.getKey(), object.getInputStream(), forcedPut);
                    if (StrUtil.isNotEmpty(object.getOriginalFileName())) {
                        sObject.setOriginalFileName(object.getOriginalFileName());
                    }
                    return sObject;
                }
        ).collect(Collectors.toList());
    }

    SObject doGet(String key);

    void doRemove(String key);

    void doBatchRemove(List<String> keyList);
}