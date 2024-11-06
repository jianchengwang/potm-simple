package org.example.potm.framework.config.permission.menu;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.pojo.PojoConverter;
import org.example.potm.framework.utils.JSONUtils;

import java.util.*;
import java.util.List;

@Slf4j
@Deprecated
public class MenuOperator {
    @Getter
    private MenuJson defaultMenuJson;
    @Getter
    private List defaultAdminAsyncRoutes;
    @Getter
    private MenuJson menuJson;
    @Getter
    private List adminAsyncRoutes;
    @Getter
    private final List<MenuItem> menuList = new ArrayList<>();
    private final Map<String, List<String>> menuPermissionsMap = new HashMap<>();
    private final Map<String, String> menuCodeMap = new HashMap<>();
    @Getter
    private final List<RouteItem> routeList = new ArrayList<>();

    private final Object lockObj = new Object();

    public MenuOperator(org.springframework.core.io.Resource menuResource, org.springframework.core.io.Resource adminAsyncRoutesResource) {
        try {
            this.defaultMenuJson = JSONUtils.OBJECT_MAPPER.readValue(menuResource.getInputStream(), MenuJson.class);
            this.defaultAdminAsyncRoutes = JSONUtils.OBJECT_MAPPER.readValue(adminAsyncRoutesResource.getInputStream(), List.class);
            this.menuJson = JSONUtils.OBJECT_MAPPER.convertValue(defaultMenuJson, MenuJson.class);
            this.adminAsyncRoutes = JSONUtils.OBJECT_MAPPER.convertValue(defaultAdminAsyncRoutes, List.class);
            rebuild(menuJson, adminAsyncRoutes);
        } catch (Exception e) {
//            log.error("MenuOperator init error", e);
        }
    }

    public List<String> getMenuCodeList(List<String> menuIdList) {
        if(CollectionUtil.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }
        List<String> menuCodes = new ArrayList<>();
        menuIdList.forEach(menuId -> {
            String menuCode = menuCodeMap.get(menuId);
            if(menuCode != null) {
                menuCodes.add(menuCode);
            }
        });
        return menuCodes;
    }

    public List<String> getPermissionList(List<String> menuIdList) {
        if(CollectionUtil.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }
        List<String> permissions = new ArrayList<>();
        menuIdList.forEach(menuId -> {
            List<String> menuPermissions = menuPermissionsMap.get(menuId);
            if(CollectionUtil.isNotEmpty(menuPermissions)) {
                permissions.addAll(menuPermissions);
            }
        });
        return permissions;
    }

    public void rebuild(MenuJson mp, List adminAsyncRoutes) {
        synchronized (lockObj) {
            this.menuJson = JSONUtils.OBJECT_MAPPER.convertValue(mp, MenuJson.class);
            this.adminAsyncRoutes = JSONUtils.OBJECT_MAPPER.convertValue(adminAsyncRoutes, List.class);
            menuList.clear();
            menuCodeMap.clear();
            menuPermissionsMap.clear();
            routeList.clear();
            menuJson2MenuList(mp);
            routeFlat2RouteList(adminAsyncRoutes);
        }
    }

    private void menuJson2MenuList(MenuJson mp) {
        if(mp == null) {
            return;
        }
        MenuItem parentMenuItem = PojoConverter.convert(mp, MenuItem.class);
        menuList.add(parentMenuItem);
        menuCodeMap.put(mp.getId(), mp.getCode());
        if(CollectionUtil.isNotEmpty(mp.getPermissions())) {
            menuPermissionsMap.put(mp.getId(), mp.getPermissions());
        }
        if(CollectionUtils.isNotEmpty(mp.getChildren())) {
            mp.getChildren().forEach(this::menuJson2MenuList);
        }
    }

    private void routeFlat2RouteList(List adminAsyncRoutes) {
        if(CollectionUtils.isEmpty(adminAsyncRoutes)) {
            return;
        }
        for(Object o : adminAsyncRoutes) {
            RouteJson rj = JSONUtils.OBJECT_MAPPER.convertValue(o, RouteJson.class);
            routeJson2RouteList(rj);
        }
    }

    private void routeJson2RouteList(RouteJson rj) {
        RouteItem parentRouteItem = PojoConverter.convert(rj, RouteItem.class);
        routeList.add(parentRouteItem);
        if(CollectionUtils.isNotEmpty(rj.getChildren())) {
            rj.getChildren().forEach(this::routeJson2RouteList);
        }
    }
}
