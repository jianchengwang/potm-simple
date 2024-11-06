package org.example.potm.framework.config.dict;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictUtils {
    private static ObjectProvider<DictProvider> objectDictProvider;
    private static DictProvider dictProvider;
    public DictUtils(ObjectProvider<DictProvider> objectDictProvider) {
        DictUtils.objectDictProvider = objectDictProvider;
        dictProvider = objectDictProvider.getIfAvailable();
    }

    public static String getDictLabel(DictKeyEnum dictKeyEnum, String value) {
        if(dictProvider != null) {
            return dictProvider.getDictLabel(dictKeyEnum, value);
        }
        return null;
    }

    public static String getDictValue(DictKeyEnum dictKeyEnum, String label) {
        if(dictProvider != null) {
            return dictProvider.getDictValue(dictKeyEnum, label);
        }
        return null;
    }

    public static List<SysDictItem> getDictItemList(DictKeyEnum dictKeyEnum) {
        if(dictProvider != null) {
            return dictProvider.getDictItemList(dictKeyEnum);
        }
        return null;
    }
}
