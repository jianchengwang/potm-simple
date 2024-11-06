package org.example.potm.svc.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.potm.svc.sys.dao.SysDictDao;
import org.example.potm.svc.sys.model.dto.SysDictItemDTO;
import org.example.potm.svc.sys.model.query.SysDictQuery;
import org.example.potm.svc.sys.service.SysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.config.dict.DictRedisOperator;
import org.example.potm.framework.config.dict.SysDict;
import org.example.potm.framework.config.dict.SysDictItem;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysDictServiceImpl implements SysDictService {

    private final SysDictDao sysDictDao;
    private final DictRedisOperator dictRedisOperator;

    private final Cache<String, List<SysDict>> DictCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private final Map<String, List<SysDictItem>> DictKeyItemCache = new ConcurrentHashMap<>();
    private final Map<String, String> DictItemValueCache = new ConcurrentHashMap<>();
    private final Map<String, String> DictItemLabelCache = new ConcurrentHashMap<>();

    public IPage<SysDict> page(PageInfo pageInfo, SysDictQuery param) {
        LambdaQueryWrapper<SysDict> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, SysDict.class));
        return sysDictDao.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    @Override
    public List<SysDictItem> fetchItemList(String dictKey, boolean forceLoadFromDb) {
        List<SysDictItem> itemList;
        if(forceLoadFromDb) {
            itemList = sysDictDao.getItemList(null, dictKey);
        } else {
            itemList = DictKeyItemCache.get(dictKey);
            if(itemList == null) {
                itemList = sysDictDao.getItemList(null, dictKey);
            }
        }
        return itemList;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateItemList(String dictKey, List<SysDictItemDTO> itemList) {
        if(CollectionUtils.isEmpty(itemList)) {
            throw new ClientException("字典项不能为空", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        if(itemList.stream().map(SysDictItemDTO::getValue).distinct().toList().size() != itemList.size()) {
            throw new ClientException("字典项值不能重复", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        List<SysDict> dictList = fetchAll(true);
        SysDict dict = dictList.stream().filter(item -> Objects.equals(item.getDictKey(), dictKey)).findFirst().orElse(null);
        if(dict == null) {
            throw new ClientException("字典不存在", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        if(dict.getSystemFlag()!=null && dict.getSystemFlag()) {
            throw new ClientException("系统字典不允许修改字典项", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        List<SysDictItem> insertList = new ArrayList<>();
        for(int i=0; i<itemList.size(); i++) {
            SysDictItemDTO itemDTO = itemList.get(i);
            SysDictItem item = dto2Po(dict, itemDTO);
            item.setSortOrder(i);
            List<SysDictItem> subItemList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(itemDTO.getChildren())) {
                for(int j=0; j<itemDTO.getChildren().size(); j++) {
                    SysDictItemDTO subItemDTO = itemDTO.getChildren().get(j);
                    SysDictItem subItem = dto2Po(dict, subItemDTO);
                    subItem.setSortOrder(j);
                    subItem.setParentItemValue(item.getValue());
                    subItemList.add(subItem);
                }
            }
            insertList.add(item);
            insertList.addAll(subItemList);
        }
        sysDictDao.deleteItemList(dict.getSvcName(), dict.getDictKey());
        sysDictDao.batchInsertItemList(insertList);
        // 更新缓存
        fetchAll(true);
    }

    private SysDictItem dto2Po(SysDict dict, SysDictItemDTO itemDTO) {
        SysDictItem item = new SysDictItem();
        BeanUtil.copyProperties(itemDTO, item);
        item.setSvcName(dict.getSvcName());
        item.setDictKey(dict.getDictKey());
        item.setType(null);
        if(item.getRemark() == null) {
            item.setRemark(item.getLabel());
        }
        item.setSystemFlag(false);
        return item;
    }

    public List<SysDict> fetchAll(boolean forceLoadFromDb) {
        String DICT_CACHE_KEY = "dict_cache";
        List<SysDict> dictList;
        if(!forceLoadFromDb) {
            dictList = DictCache.getIfPresent(DICT_CACHE_KEY);
            if(dictList ==null) {
                dictList = dictRedisOperator.loadAll();
            }
            if(CollectionUtil.isNotEmpty(dictList)) {
                return dictList;
            }
        }
        dictList = sysDictDao.fetchAll();
        List<SysDictItem> itemList = sysDictDao.fetchItemAll();
        for(SysDict dict: dictList) {
            List<SysDictItem> subItemList = itemList.stream()
                    .filter(item -> Objects.equals(item.getSvcName(), dict.getSvcName()) && Objects.equals(item.getDictKey(), dict.getDictKey()))
                    .sorted(Comparator.comparing(SysDictItem::getSortOrder)).toList();
            dict.setItemList(subItemList);
            DictKeyItemCache.put(dict.getDictKey(), subItemList);
            subItemList.parallelStream().forEach(item -> DictItemValueCache.put(item.getDictKey() + "_" + item.getValue(), item.getLabel()));
            subItemList.parallelStream().forEach(item -> DictItemLabelCache.put(item.getDictKey() + "_" + item.getLabel(), item.getValue()));
        }
        DictCache.put(DICT_CACHE_KEY, dictList);
        dictRedisOperator.putAll(dictList);
        return dictList;
    }

    @Override
    public String getDictLabel(DictKeyEnum dictKeyEnum, Object value) {
        return DictItemValueCache.get(dictKeyEnum.getKey() + "_" + value);
    }

    @Override
    public String getDictValue(DictKeyEnum dictKeyEnum, String label) {
        return DictItemLabelCache.get(dictKeyEnum.getKey() + "_" + label.trim());
    }

    @Override
    public List<SysDictItem> getDictItemList(DictKeyEnum dictKeyEnum) {
        return DictKeyItemCache.get(dictKeyEnum.getKey());
    }

    @Override
    public void clear() {
        DictItemValueCache.clear();
        DictItemLabelCache.clear();
        DictKeyItemCache.clear();
        DictCache.invalidateAll();
        dictRedisOperator.clear();
    }
}
