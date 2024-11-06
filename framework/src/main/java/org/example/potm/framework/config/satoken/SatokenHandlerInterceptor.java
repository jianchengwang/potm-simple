package org.example.potm.framework.config.satoken;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.config.permission.user.TokenUser;
import org.example.potm.framework.config.permission.user.TokenUserContextHolder;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@Slf4j
public class SatokenHandlerInterceptor extends SaInterceptor {

    public SatokenHandlerInterceptor() {
        super();
        this.auth = handler -> {
            SaRouter.match("/api/**", "", r -> StpUtil.checkLogin());
        };
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先关闭权限校验
        try {
            // 默认管理员账号通过所有权限
            if(TokenUserContextHolder.currentUserIsAdmin()) {
                return true;
            }
            if(handler instanceof HandlerMethod) {
                if (this.isAnnotation) {
                    Method method = ((HandlerMethod)handler).getMethod();
                    if (SaStrategy.me.isAnnotationPresent.apply(method, SaIgnore.class)) {
                        return true;
                    }
                    SaStrategy.me.checkMethodAnnotation.accept(method);
                }
                this.auth.run(handler);
            }
        } catch (Exception e) {
            if(e instanceof NotLoginException) {
                throw new ClientException(FrameworkErrorCode.UN_AUTHORIZED);
            }
            else throw new ClientException(FrameworkErrorCode.USER_NOT_PERMISSION);
        }
        return true;
    }

}

