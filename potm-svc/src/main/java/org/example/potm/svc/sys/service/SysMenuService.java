package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.potm.svc.sys.model.dto.SysMenuConfigSaveDTO;
import org.example.potm.svc.sys.model.dto.SysMenuSaveDTO;
import org.example.potm.svc.sys.model.po.SysMenuConfig;
import org.example.potm.svc.sys.model.vo.SysMenuVO;
import org.example.potm.framework.config.permission.menu.RouteJson;

import java.util.List;
import java.util.Set;

public interface SysMenuService<SysMenu> extends IService<SysMenu> {
    SysMenuConfig getMenuConfig();
    void updateMenuConfig(SysMenuConfigSaveDTO param);

    void resetDefaultMenuConfig();

    List<SysMenuVO> getMenuList();

    void saveOrUpdate(SysMenuSaveDTO param);

    void deleteById(String id);

    List<RouteJson> getAdminAsyncRoutes();

    Set<String> getAllMenuIdListWithParentId(List<String> menuIdList);

    List<String> getPermissionList(List<String> menuIdList);
}
