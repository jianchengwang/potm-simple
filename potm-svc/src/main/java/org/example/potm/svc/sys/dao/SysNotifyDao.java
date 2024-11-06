package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.po.SysNotify;
import org.example.potm.svc.sys.model.query.SysNotifyQuery;
import org.example.potm.svc.sys.model.vo.SysNotifyVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统通知 Mapper 接口
 * </p>
 *
 * @author jianchengwang
 * @since 2023-12-26
 */
@Mapper
public interface SysNotifyDao extends BaseMapper<SysNotify> {
    IPage<SysNotifyVO> page(IPage<SysNotify> page, SysNotifyQuery param, LambdaQueryWrapper<SysNotify> ew);
}