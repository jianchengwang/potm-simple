package org.example.potm.svc.auth.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.example.potm.svc.auth.constant.AuthErrorCode;
import org.example.potm.svc.auth.service.AuthService;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import org.example.potm.svc.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.config.permission.user.SysUser;
import org.example.potm.framework.config.permission.user.TokenUser;
import org.example.potm.framework.config.permission.user.TokenUserContextHolder;
import org.example.potm.framework.config.permission.user.UserStatusEnum;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PojoConverter;
import org.springframework.stereotype.Service;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService userService;

    public TokenUser login(String username, String password) {
        SysUser user = userService.findByUsername(username);
        if(user == null) {
            throw new ClientException("用户不存在", FrameworkErrorCode.RESOURCE_NOT_FOUND);
        }
        String checkPassword = SaSecureUtil.md5BySalt(password, user.getPasswordSalt());
        if(!user.getPassword().equals(checkPassword)) {
            throw new ClientException(AuthErrorCode.USER_PASSWORD_ERROR);
        }
        if(UserStatusEnum.ENABLE != user.getUserStatus()) {
            throw new ClientException(AuthErrorCode.USER_NOT_NORMAL);
        }
        SysUserVO userVO = userService.getById(user.getId(), true);
        // 登录，用户编号作为loginId
        StpUtil.login(userVO.getId());
        SaTokenInfo token = StpUtil.getTokenInfo();

        // 放到用户上下文
        TokenUser tokenUser = PojoConverter.convert(userVO, TokenUser.class);
        tokenUser.setAccessToken(token.getTokenValue());
        TokenUserContextHolder.setCurrentUser(tokenUser);
        return tokenUser;
    }

    public void logout() {
        StpUtil.logout();
    }
}
