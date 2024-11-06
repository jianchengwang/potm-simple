package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.query.SysRoleQuery;
import org.example.potm.svc.sys.model.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {
    IPage<SysRoleVO> page(IPage<SysRole> page, SysRoleQuery param, LambdaQueryWrapper<SysRole> ew);

    List<SysRoleVO> fetchAll();
}
