package org.example.potm.svc.auth.service;

import org.example.potm.framework.config.permission.user.TokenUser;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
public interface AuthService {

    TokenUser login(String username, String password);

    void logout();
}
