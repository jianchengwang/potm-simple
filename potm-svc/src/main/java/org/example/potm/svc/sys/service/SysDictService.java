package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.dto.SysDictItemDTO;
import org.example.potm.svc.sys.model.query.SysDictQuery;
import org.example.potm.framework.config.dict.DictProvider;
import org.example.potm.framework.config.dict.SysDict;
import org.example.potm.framework.config.dict.SysDictItem;
import org.example.potm.framework.pojo.PageInfo;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
public interface SysDictService extends DictProvider {

    IPage<SysDict> page(PageInfo pageInfo, SysDictQuery param);

    List<SysDictItem> fetchItemList(String dictKey, boolean forceLoadFromDb);

    void updateItemList(String dictKey, List<SysDictItemDTO> itemList);
}
