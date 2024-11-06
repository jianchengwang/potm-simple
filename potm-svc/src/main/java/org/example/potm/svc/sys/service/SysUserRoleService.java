package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.po.SysUserRole;
import org.example.potm.framework.config.permission.user.SysUser;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    List<SysRole> getByUserId(Long userId);
    void updateByUserId(Long userId, List<Long> roleIdList);
    List<SysUser> getByRoleId(Long roleId);
}
