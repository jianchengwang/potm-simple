package org.example.potm.framework.config.permission.user;

import cn.dev33.satoken.stp.StpUtil;
import org.example.potm.framework.config.permission.PermissionConstant;

import java.util.Objects;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
public final class TokenUserContextHolder {
    private static final String TOKEN_USER_KEY = "tokenUser";

    public static Long currentUserId() {
        try {
            return StpUtil.getLoginId(0L);
        } catch (Exception ignored) {
        }
        return 0L;
    }

    public static boolean currentUserIsAdmin() {
        try {
            return Objects.equals(StpUtil.getLoginIdAsLong(), PermissionConstant.DEFAULT_ADMIN_USERID);
        } catch (Exception ignored) {
        }
        return false;
    }

    public static TokenUser currentUser() {
        try {
            return StpUtil.getSessionByLoginId(currentUserId()).get(TOKEN_USER_KEY, new TokenUser());
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void setCurrentUser(TokenUser tokenUser) {
        StpUtil.getSessionByLoginId(currentUserId()).set(TOKEN_USER_KEY, tokenUser);
    }
}
