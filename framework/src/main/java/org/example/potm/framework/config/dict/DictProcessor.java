package org.example.potm.framework.config.dict;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.example.potm.framework.pojo.IBaseEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Slf4j
public class DictProcessor {
    private final String configSvcName;
    private final DictProperties dictProperties;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectProvider<DictProvider> objectDictProvider;

    public DictProcessor(String configSvcName, DictProperties dictProperties,
                         JdbcTemplate jdbcTemplate,
                         ObjectProvider<DictProvider> objectDictProvider) {
        this.configSvcName = configSvcName;
        this.dictProperties = dictProperties;
        this.jdbcTemplate = jdbcTemplate;
        this.objectDictProvider = objectDictProvider;
    }

    @PostConstruct
    public void syncDb() {
        Object[] enumsPackages = dictProperties.getEnumsPackage().split(",");
        Reflections reflections = new Reflections(enumsPackages);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(DictEnum.class);
        List<SysDict> dictList = new ArrayList<>();
        List<SysDictItem> dictItemList = new ArrayList<>();
        // 同步枚举字典
        Map<String, Class> dictEnumMap = new HashMap<>();
        for(Class<?> clazz: classesList) {
            DictEnum dictEnum = clazz.getAnnotation(DictEnum.class);
            dictEnumMap.put(dictEnum.dictKey().getKey(), clazz);
        }
        // 同步自定义字典
        DictKeyEnum[] dictKeyEnums = DictKeyEnum.values();
        for(DictKeyEnum dictKeyEnum: dictKeyEnums) {
            String dictKey = dictKeyEnum.getKey();
            String svcName = dictKeyEnum.getSvc();
            if(StringUtils.isEmpty(svcName)) {
                svcName = configSvcName;
            }
            // 如果不匹配跳过
            if(!Objects.equals(svcName, configSvcName) && !Objects.equals(svcName, "default")) {
                continue;
            }
            Boolean systemFlag = dictKeyEnum.getSystemFlag();
            Boolean enumFlag = dictKeyEnum.getEnumFlag();
            Integer sortOrder = dictKeyEnum.getSortOrder();
            List<SysDictItem> builtinItemList = dictKeyEnum.getItemList();
            SysDict dict = new SysDict(svcName, dictKey, dictKeyEnum.getDescription(), dictKeyEnum.getRemark(), systemFlag, enumFlag, sortOrder);
            if(dictEnumMap.containsKey(dictKey)) {
                dict.setSystemFlag(true);
                dict.setEnumFlag(true);
                Class clazz = dictEnumMap.get(dictKey);
                IBaseEnum[] itemEnumList = (IBaseEnum[]) clazz.getEnumConstants();
                List<SysDictItem> itemList = new ArrayList<>();
                var itemSortOrder = 0;
                for(IBaseEnum itemEnum: itemEnumList) {
                    SysDictItem item = new SysDictItem(svcName, dictKey, itemEnum, itemSortOrder++);
                    itemList.add(item);
                }
                dictList.add(dict);
                dictItemList.addAll(itemList);
            } else {
                dictList.add(dict);
                if(dict.getSystemFlag()!=null && dict.getSystemFlag() && CollectionUtils.isNotEmpty(builtinItemList)) {
                    dictItemList.addAll(builtinItemList);
                }
            }
        }
        try {
            if(dictProperties.isSyncDb()) {
                List<Object[]> dictRecords = dictList.stream()
                        .sorted(Comparator.comparing(SysDict::getSortOrder))
                        .map(r -> new Object[]{
                            r.getSvcName(), r.getDictKey(), r.getDescription(), r.getRemark(), r.getSystemFlag(), r.getEnumFlag()
                        })
                        .collect(Collectors.toList());

                List<Object[]> dictItemRecords = dictItemList.stream()
                        .map(r -> new Object[] {
                                r.getSvcName(), r.getDictKey(), r.getValue(), r.getLabel(), r.getType(), r.getSortOrder(), r.getSystemFlag(),
                                r.getRemark(), r.getColor(),
                                r.getParentItemValue()
                        })
                        .collect(Collectors.toList());
                jdbcTemplate.execute(DictSqlTemplate.DELETE_SYSTEM_DICT_SQL);
                jdbcTemplate.batchUpdate(DictSqlTemplate.REPLACE_INSERT_DICT_SQL, dictRecords);
                jdbcTemplate.batchUpdate(DictSqlTemplate.REPLACE_INSERT_DICT_ITEM_SQL, dictItemRecords);
            }
            DictProvider dictProvider = objectDictProvider.getIfAvailable();
            if (dictProvider != null) {
                dictProvider.clear();
                dictProvider.fetchAll(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步枚举字典异常：{}", e.getMessage());
        }
    }
}
