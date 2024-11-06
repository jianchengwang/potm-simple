package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaIgnore;
import org.example.potm.svc.auth.model.dto.LoginByUsernameDTO;
import org.example.potm.svc.auth.service.AuthService;
import org.example.potm.svc.sys.dao.SysUserDao;
import org.example.potm.svc.sys.model.dto.SysUserUpdatePasswordDTO;
import org.example.potm.svc.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.cdc.NotLog;
import org.example.potm.framework.config.permission.user.TokenUser;
import org.example.potm.framework.config.permission.user.TokenUserContextHolder;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块-用户模块")
public class AuthController {

    private final AuthService authService;
    private final SysUserService userService;
    private final SysUserDao sysUserDao;

    @Operation(summary = "用户名密码登录", description = "用户名密码登录")
    @PostMapping("loginByUsername")
    @SaIgnore
    @NotLog
    public Response<TokenUser> login(@Valid @RequestBody LoginByUsernameDTO loginParam) {
        return Response.ok(authService.login(loginParam.getUsername(), loginParam.getPassword()));
    }

    @Operation(summary = "退出登录", description = "退出登录")
    @GetMapping("logout")
    @SaIgnore
    public Response<Void> logout() {
        authService.logout();
        return Response.ok();
    }

    @Operation(summary = "修改密码", description = "修改密码")
    @PostMapping("updatePassword")
    public Response<Void> updatePassword(@RequestBody SysUserUpdatePasswordDTO param) {
        userService.updatePassword(TokenUserContextHolder.currentUserId(), param);
        return Response.ok();
    }
}
