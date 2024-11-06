package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.potm.svc.sys.model.po.SysNotify;
import org.example.potm.svc.sys.model.query.SysNotifyQuery;
import org.example.potm.svc.sys.model.vo.SysNotifyVO;
import org.example.potm.framework.pojo.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统通知 服务类
 * </p>
 *
 * @author jianchengwang
 * @since 2023-12-26
 */
public interface SysNotifyService extends IService<SysNotify> {
    IPage<SysNotifyVO> page(PageInfo pageInfo, SysNotifyQuery param);
    void batchInsert(List<SysNotify> insertList);
    void deleteById(Serializable id);

    void batchRead(Long userId, List<String> idList);

    void batchDelete(Long userId, List<String> idList);
}
