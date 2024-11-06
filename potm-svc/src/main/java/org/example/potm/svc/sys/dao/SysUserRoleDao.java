package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.po.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.example.potm.framework.config.permission.user.SysUser;

import java.util.List;

@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {
    List<SysRole> getByUserId(Long userId);

    List<SysUser> getByRoleId(Long roleId);
}
