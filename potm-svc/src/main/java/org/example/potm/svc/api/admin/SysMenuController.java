package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.example.potm.svc.sys.model.dto.SysMenuConfigSaveDTO;
import org.example.potm.svc.sys.model.dto.SysMenuSaveDTO;
import org.example.potm.svc.sys.model.po.SysMenuConfig;
import org.example.potm.svc.sys.model.vo.SysMenuVO;
import org.example.potm.svc.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.permission.menu.MenuJson;
import org.example.potm.framework.config.permission.menu.MenuOperator;
import org.example.potm.framework.pojo.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sys/menu")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-菜单管理")
public class SysMenuController {

    private final MenuOperator menuOperator;
    private final SysMenuService service;

    @Operation(summary = "获取菜单配置", description = "获取菜单配置")
    @GetMapping("menuJson")
    public Response<MenuJson> getMenuJson() {
        return Response.ok(menuOperator.getMenuJson());
    }

    @Operation(summary = "获取默认菜单配置", description = "获取默认菜单配置")
    @GetMapping("defaultMenuJson")
    public Response<MenuJson> getDefaultMenuJson() {
        return Response.ok(menuOperator.getDefaultMenuJson());
    }

    @Operation(summary = "获取管理平台动态路由", description = "获取管理平台动态路由")
    @GetMapping("adminAsyncRoutes")
    public Response<List> getAdminAsyncRoutes() {
        return Response.ok(menuOperator.getDefaultAdminAsyncRoutes());
    }

    @Operation(summary = "获取管理平台动态路由根据菜单动态获取", description = "获取管理平台动态路由根据菜单动态获取")
    @GetMapping("adminAsyncRoutes2")
    public Response<List> adminAsyncRoutes2() {
        return Response.ok(service.getAdminAsyncRoutes());
    }

    @Operation(summary = "获取默认管理平台路由", description = "获取默认管理平台路由")
    @GetMapping("defaultAdminAsyncRoutes")
    public Response<List> getDefaultAdminAsyncRoutes() {
        return Response.ok(menuOperator.getDefaultAdminAsyncRoutes());
    }

    @Operation(summary = "获取菜单配置", description = "获取菜单配置")
    @GetMapping("menuConfig")
    public Response<SysMenuConfig> getMenuConfig() {
        return Response.ok(service.getMenuConfig());
    }

    @SaCheckPermission("admin:sys:menu:post")
    @Operation(summary = "更新菜单配置", description = "更新菜单配置")
    @PostMapping("menuConfig")
    public Response<Void> updateMenuConfig(@Validated @RequestBody SysMenuConfigSaveDTO param) {
        service.updateMenuConfig(param);
        return Response.ok();
    }

    @SaCheckPermission("admin:sys:menu:post")
    @Operation(summary = "恢复初始配置", description = "恢复初始配置")
    @PostMapping("resetDefaultMenuConfig")
    public Response<Void> resetDefaultMenuConfig() {
        service.resetDefaultMenuConfig();
        return Response.ok();
    }

    @Operation(summary = "获取菜单列表", description = "获取菜单列表")
    @GetMapping("list")
    public Response<List<SysMenuVO>> getMenuList() {
        return Response.ok(service.getMenuList());
    }

    @Operation(summary = "更新菜单项", description = "更新菜单项")
    @PostMapping("")
    public Response<Void> saveOrUpdate(@Valid @RequestBody SysMenuSaveDTO param) {
        service.saveOrUpdate(param);
        return Response.ok();
    }

    @Operation(summary = "删除菜单项", description = "删除菜单项")
    @DeleteMapping("{id}")
    public Response<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return Response.ok();
    }

}
