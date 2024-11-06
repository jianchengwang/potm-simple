package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.SysDictQuery;
import org.apache.ibatis.annotations.Mapper;
import org.example.potm.framework.config.dict.SysDict;
import org.example.potm.framework.config.dict.SysDictItem;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDict> {
    IPage<SysDict> page(IPage<SysDict> page, SysDictQuery param, LambdaQueryWrapper<SysDict> ew);

    List<SysDictItem> getItemList(String svcName, String dictKey);

    List<SysDict> fetchAll();

    List<SysDictItem> fetchItemAll();

    void deleteItemList(String svcName, String dictKey);

    void batchInsertItemList(List<SysDictItem> itemList);
}
