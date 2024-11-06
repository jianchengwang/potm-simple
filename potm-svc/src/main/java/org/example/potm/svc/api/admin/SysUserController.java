package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.dao.SysUserDao;
import org.example.potm.svc.sys.model.dto.SysUserSaveDTO;
import org.example.potm.svc.sys.model.query.SysUserQuery;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import org.example.potm.svc.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
@RestController
@RequestMapping("/api/admin/sys/user")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-用户管理")
public class SysUserController {

    private final SysUserService userService;

    private final SysUserDao sysUserDao;

    @SaCheckPermission("admin:sys:user:query")
    @Operation(summary = "列表", description = "列表")
    @GetMapping("list")
    public Response<List<SysUserVO>> list(SysUserQuery param) {
        return Response.ok(userService.list(param));
    }

    @SaCheckPermission("admin:sys:user:query")
    @Operation(summary = "分页", description = "分页")
    @GetMapping("page")
    public Response<IPage<SysUserVO>> page(PageInfo pageInfo, SysUserQuery param) {
        IPage<SysUserVO> iPage = userService.page(pageInfo, param);
        return Response.ok(iPage);
    }

    @SaCheckPermission("admin:sys:user:query")
    @Operation(summary = "详情", description = "详情")
    @GetMapping("{id}")
    public Response<SysUserVO> getById(@PathVariable Long id) {
        SysUserVO sysUserVO = userService.getById(id, true);
        return Response.ok(sysUserVO);
    }

    @SaCheckPermission("admin:sys:user:query")
    @Operation(summary = "用户角色编号集合", description = "用户角色编号集合")
    @GetMapping("{id}/roleIds")
    public Response<List<Long>> getRoleIds(@PathVariable Long id) {
        return Response.ok(userService.getRoleIds(id));
    }

    @SaCheckPermission("admin:sys:user:update")
    @Operation(summary = "保存", description = "保存")
    @PostMapping("")
    @SaIgnore
    public Response<Object> save(@Valid @RequestBody SysUserSaveDTO param) {
        SysUserVO sysUserVO = userService.save(param);
        return Response.ok(sysUserVO);
    }

    @SaCheckPermission("admin:sys:user:update")
    @Operation(summary = "删除", description = "删除")
    @DeleteMapping("{id}")
    public Response<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Response.ok();
    }

}
