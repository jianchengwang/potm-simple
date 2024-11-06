package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.dto.SysRoleSaveDTO;
import org.example.potm.svc.sys.model.query.SysRoleQuery;
import org.example.potm.svc.sys.model.vo.SysRoleVO;
import org.example.potm.svc.sys.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sys/role")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-角色管理")
public class SysRoleController {

    private final SysRoleService service;

    @Operation(summary = "分页", description = "分页")
    @GetMapping("page")
    public Response<IPage<SysRoleVO>> page(PageInfo pageInfo, SysRoleQuery param) {
        return Response.ok(service.page(pageInfo, param));
    }

    @Operation(summary = "列表", description = "列表")
    @GetMapping("list")
    public Response<List<SysRoleVO>> fetchAll() {
        return Response.ok(service.fetchAll());
    }

    @SaCheckPermission("admin:sys:role:update")
    @Operation(summary = "保存", description = "保存")
    @PostMapping("")
    public Response<Void> save(@Valid @RequestBody SysRoleSaveDTO param) {
        service.save(param);
        return Response.ok();
    }

    @SaCheckPermission("admin:sys:role:update")
    @Operation(summary = "删除", description = "删除")
    @DeleteMapping("{id}")
    public Response<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return Response.ok();
    }

    @Operation(summary = "角色菜单集合", description = "角色菜单集合")
    @GetMapping("{id}/menuIds")
    public Response<List<String>> getMenuIds(@PathVariable Long id) {
        return Response.ok(service.getMenuIds(id));
    }
}
