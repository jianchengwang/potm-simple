package org.example.potm.framework.config.dict;

import java.util.List;

public interface DictProvider {
    List<SysDict> fetchAll(boolean forceLoadFromDb);
    String getDictLabel(DictKeyEnum dictKeyEnum, Object value);
    String getDictValue(DictKeyEnum dictKeyEnum, String label);
    List<SysDictItem> getDictItemList(DictKeyEnum dictKeyEnum);
    void clear();
}
