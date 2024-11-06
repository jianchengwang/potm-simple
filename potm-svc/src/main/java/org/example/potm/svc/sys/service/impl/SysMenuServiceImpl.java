package org.example.potm.svc.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.sys.cache.SysMenuCache;
import org.example.potm.svc.sys.dao.SysMenuConfigDao;
import org.example.potm.svc.sys.dao.SysMenuDao;
import org.example.potm.svc.sys.model.dto.SysMenuConfigSaveDTO;
import org.example.potm.svc.sys.model.dto.SysMenuSaveDTO;
import org.example.potm.svc.sys.model.po.SysMenu;
import org.example.potm.svc.sys.model.po.SysMenuConfig;
import org.example.potm.svc.sys.model.vo.SysMenuVO;
import org.example.potm.svc.sys.service.SysMenuService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.config.permission.menu.*;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PojoConverter;
import org.example.potm.framework.utils.JSONUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService<SysMenu> {
    private final Long DEFAULT_MENU_CONFIG_ID = 1L;
    private final MenuOperator menuOperator;
    private final SysMenuCache menuCache;
    private final SysMenuConfigDao menuConfigDao;
    private final SysMenuDao menuDao;

    public void mergeMenuAndRoute() {
        List<SysMenu> menuList = new ArrayList<>();
        List<MenuItem> menuItemList = menuOperator.getMenuList();
        List<RouteItem> routeItemList = menuOperator.getRouteList();
        for(MenuItem menuItem : menuItemList) {
            SysMenu sysMenu = PojoConverter.convert(menuItem, SysMenu.class);
            sysMenu.setPermissions(String.join(",", menuItem.getPermissions()));
            String menuCode = menuItem.getCode();
            RouteItem routeItem = routeItemList.stream().filter(item -> item.getMeta()!=null && item.getMeta().getMenuCode().equals(menuCode)).findFirst().orElse(null);
            if(routeItem != null) {
                sysMenu.setRouteName(routeItem.getName());
                sysMenu.setRoutePath(routeItem.getPath());
                RouteMeta routeMeta = routeItem.getMeta();
                if(routeMeta != null) {
                    sysMenu.setRouteMetaIcon(routeMeta.getIcon());
                    sysMenu.setRouteMetaKeepAlive(routeMeta.getKeepAlive());
                    sysMenu.setRouteMetaFrameSrc(routeMeta.getFrameSrc());
                }
            }
            menuList.add(sysMenu);
        }
        menuDao.truncateMenuTable();
        this.saveBatch(menuList);
        menuCache.clearList();
    }

    @Override
    public SysMenuConfig getMenuConfig() {
        SysMenuConfig menuConfig = menuCache.load();
        if(menuConfig == null) {
            menuConfig = menuConfigDao.selectById(1);
        }
        return menuConfig;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateMenuConfig(SysMenuConfigSaveDTO param) {
        SysMenuConfig menuConfig = PojoConverter.convert(param, SysMenuConfig.class);
        menuConfig.setId(DEFAULT_MENU_CONFIG_ID);
        if(StringUtils.isEmpty(menuConfig.getMenuJson())) {
            menuConfig.setMenuJson(JSONUtils.jsonString(menuOperator.getMenuJson()));
        }
        if(StringUtils.isEmpty(menuConfig.getAdminAsyncRoutesJson())) {
            menuConfig.setAdminAsyncRoutesJson(JSONUtils.jsonString(menuOperator.getAdminAsyncRoutes()));
        }
        menuConfigDao.updateById(menuConfig);
        menuCache.put(menuConfig);
        menuOperator.rebuild(JSONUtils.json2Object(menuConfig.getMenuJson(), MenuJson.class), JSONUtils.json2Object(menuConfig.getAdminAsyncRoutesJson(), List.class));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void resetDefaultMenuConfig() {
//        SysMenuConfigSaveDTO param = new SysMenuConfigSaveDTO();
//        param.setMenuJson(JSONUtils.jsonString(menuOperator.getDefaultMenuJson()));
//        param.setAdminAsyncRoutesJson(JSONUtils.jsonString(menuOperator.getDefaultAdminAsyncRoutes()));
//        this.updateMenuConfig(param);
        menuOperator.rebuild(menuOperator.getDefaultMenuJson(), menuOperator.getDefaultAdminAsyncRoutes());
        mergeMenuAndRoute();
    }

    @Override
    public List<SysMenuVO> getMenuList() {
        List<SysMenu> menuList = menuCache.loadList();
        if(CollectionUtils.isEmpty(menuList)) {
            menuList = this.list();
            menuCache.putList(menuList);
        }
        return PojoConverter.conventList(menuList, SysMenuVO.class);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveOrUpdate(SysMenuSaveDTO param) {
        SysMenu updateObj = PojoConverter.convert(param, SysMenu.class);
        this.saveOrUpdate(updateObj);
        menuCache.clearList();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteById(String id) {
        SysMenu getObj = this.getById(id);
        if(getObj == null) {
            throw new ClientException("菜单不存在", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        // 判断子菜单是否存在
        LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysMenu::getParentId, id);
        List<SysMenu> childMenuList = this.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(childMenuList)) {
            throw new ClientException("存在子菜单，无法删除", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        this.removeById(id);
        menuCache.clearList();
    }

    @Override
    public List<RouteJson> getAdminAsyncRoutes() {
        List<SysMenu> menuList = PojoConverter.conventList(this.getMenuList(),SysMenu.class);
        menuList = menuList.stream()
                .filter(menu -> Objects.equals(menu.getSourceType(), "admin") && Objects.equals(menu.getHidden(), false) && Objects.equals(menu.getButtonFlag(), false))
                .collect(Collectors.toList());
        Comparator<SysMenu> customComparator = Comparator
                .comparingLong((SysMenu menu) -> (StringUtils.isEmpty(menu.getParentId()) ? 0L : Long.parseLong(menu.getParentId())))
                .thenComparingInt(SysMenu::getSortOrder);
        menuList.sort(customComparator);
        List<TreeNode<String>> treeNodeList = new ArrayList<>();
        menuList.forEach(treeData -> {
            TreeNode<String> treeNode = new TreeNode<>();
            treeNode.setId(treeData.getId());
            treeNode.setParentId(treeData.getParentId());
            treeNodeList.add(treeNode);
        });
        Map<String, SysMenu> menuMap = menuList.stream().collect(Collectors.toMap(SysMenu::getId, menu -> menu));
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setDeep(5);
        Tree<String> menuTree = TreeUtil.buildSingle(treeNodeList, "1", treeNodeConfig, new DefaultNodeParser<>());
        MenuJson treeData = JSONUtils.json2Object(JSONUtils.jsonString(menuTree), MenuJson.class);
        List<RouteJson> routeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(treeData.getChildren())) {
            for(MenuJson menuJson : treeData.getChildren()) {
                routeList.add(convertMenu2Route(menuJson, menuMap));
            }
        }
        return routeList;
    }

    private RouteJson convertMenu2Route(MenuJson menuJson, Map<String, SysMenu> menuMap) {
        SysMenu menuVO = menuMap.get(menuJson.getId());
        RouteJson routeJson = new RouteJson();
        if(StringUtils.isNotEmpty(menuVO.getRouteName())) {
            routeJson.setName(menuVO.getRouteName());
        }
        if(StringUtils.isNotEmpty(menuVO.getRoutePath())) {
            routeJson.setPath(menuVO.getRoutePath());
        }
        RouteMeta routeMeta = new RouteMeta();
        routeMeta.setMenuId(menuVO.getId());
        if(StringUtils.isNotEmpty(menuVO.getRouteMetaIcon())) {
            routeMeta.setIcon(menuVO.getRouteMetaIcon());
        }
        routeMeta.setTitle(menuVO.getName());
        if(menuVO.getRouteMetaKeepAlive() != null) {
            routeMeta.setKeepAlive(menuVO.getRouteMetaKeepAlive());
        }
        if(StringUtils.isNotEmpty(menuVO.getRouteMetaFrameSrc())) {
            routeMeta.setFrameSrc(menuVO.getRouteMetaFrameSrc());
        }
        if(menuVO.getSortOrder() != null && menuVO.getSortOrder() > 0) {
            routeMeta.setRank(menuVO.getSortOrder());
        }
        if(menuVO.getCode() != null) {
            routeMeta.setMenuCode(menuVO.getCode());
        }
        routeMeta.setShowLink(true);
        if(menuVO.getShowLink() != null && !menuVO.getShowLink()) {
            routeMeta.setShowLink(false);
            if(StringUtils.isNotEmpty(menuVO.getActivePath())) {
                routeMeta.setActivePath(menuVO.getActivePath());
            }
        }
        routeJson.setMeta(routeMeta);
        List<RouteJson> routeChild = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(menuJson.getChildren())) {
            for(MenuJson child : menuJson.getChildren()) {
                routeChild.add(convertMenu2Route(child, menuMap));
            }
            routeJson.setChildren(routeChild);
        }
        return routeJson;
    }

    @Override
    public Set<String> getAllMenuIdListWithParentId(List<String> menuIdList) {
        List<SysMenuVO> menuList = getMenuList();
        Set<String> menuIdListWithParentId = new HashSet<>();
        for(String menuId: menuIdList) {
            Set<String> parents = getMenuIdListByParentId(menuList, menuId);
            menuIdListWithParentId.addAll(parents);
        }
        return menuIdListWithParentId;
    }

    @Override
    public List<String> getPermissionList(List<String> menuIdList) {
        if(CollectionUtil.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }
        List<SysMenuVO> menuList = getMenuList();
        Set<String> permissions = new HashSet<>();
        menuIdList.forEach(menuId -> {
            SysMenuVO menu = menuList.stream().filter(m -> m.getId().equals(menuId)).findFirst().orElse(null);
            if(menu != null) {
                if(StringUtils.isNotEmpty(menu.getPermissions())) {
                    permissions.addAll(Arrays.asList(menu.getPermissions().split(",")));
                }
            }
        });
        return permissions.stream().toList();
    }

    private Set<String> getMenuIdListByParentId(List<SysMenuVO> menuList, String menuId) {
        Set<String> parents = new HashSet<>();
        parents.add(menuId);
        SysMenuVO find = menuList.stream().filter(menu -> menu.getId().equals(menuId)).findFirst().orElse(null);
        if(find != null) {
            if (find.getParentId() != null && !find.getParentId().isEmpty() && !find.getParentId().equals("0") && !find.getParentId().equals("1")) {
                parents.addAll(getMenuIdListByParentId(menuList, find.getParentId()));
            }
        }
        return parents;
    }
}
