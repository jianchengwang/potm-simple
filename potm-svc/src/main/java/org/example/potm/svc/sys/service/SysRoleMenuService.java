package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.potm.svc.sys.model.po.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuService extends IService<SysRoleMenu> {
    List<String> getByRoleIdList(List<Long> roleIdList);
    void updateByRoleId(Long roleId, List<String> menuIdList);
    void deleteByRoleIdList(List<Long> roleIdList);
}
