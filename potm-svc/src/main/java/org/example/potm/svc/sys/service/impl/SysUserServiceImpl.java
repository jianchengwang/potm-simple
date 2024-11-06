package org.example.potm.svc.sys.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.cache.SysUserCache;
import org.example.potm.svc.sys.dao.SysUserDao;
import org.example.potm.svc.sys.model.dto.SysUserSaveDTO;
import org.example.potm.svc.sys.model.dto.SysUserUpdatePasswordDTO;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.query.SysUserQuery;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import org.example.potm.svc.sys.service.SysRoleMenuService;
import org.example.potm.svc.sys.service.SysUserRoleService;
import org.example.potm.svc.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.config.permission.PermissionConstant;
import org.example.potm.framework.config.permission.menu.MenuOperator;
import org.example.potm.framework.config.permission.user.SysUser;
import org.example.potm.framework.config.permission.user.UserScopeEnum;
import org.example.potm.framework.config.permission.user.UserStatusEnum;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserCache userCache;
    private final SysUserDao userDao;
    private final SysUserRoleService userRoleService;
    private final SysRoleMenuService roleMenuService;
    private final MenuOperator menuOperator;
    private final SysMenuServiceImpl menuService;

    @Override
    public IPage<SysUserVO> page(PageInfo pageInfo, SysUserQuery param) {
        LambdaQueryWrapper<SysUser> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, SysUser.class));
        return userDao.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    @Override
    public List<SysUserVO> list(SysUserQuery param) {
        LambdaQueryWrapper<SysUser> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, SysUser.class));
        return userDao.list(param, ew);
    }

    @Override
    public SysUserVO getById(Long id, boolean forceLoadFromDb) {
        SysUserVO user = userCache.get(id);
        if(!forceLoadFromDb && user != null) {
            return user;
        }
        user = userDao.findById(id);
        if(user == null) {
            throw new ClientException("用户不存在", FrameworkErrorCode.RESOURCE_NOT_FOUND);
        }
        // 设置角色
        List<SysRole> roleList = userRoleService.getByUserId(id);
        if(CollectionUtils.isNotEmpty(roleList)) {
            // 设置角色集合
            List<String> roles = roleList.stream().map(SysRole::getRoleCode).toList();
            user.setRoles(roles);
            List<String> menuIdList = roleMenuService.getByRoleIdList(roleList.stream().map(SysRole::getId).toList());
            // 设置菜单集合
            Set<String> menus = menuService.getAllMenuIdListWithParentId(menuIdList);
            menus.addAll(menuIdList);
            user.setMenus(menus.stream().toList());
            // 设置权限集合
            List<String> permissions = menuService.getPermissionList(menuIdList);
            user.setPermissions(permissions);
        }
        userCache.put(user);
        return user;
    }

    @Override
    public SysUser findByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public SysUserVO save(SysUserSaveDTO param) {
        if(StringUtils.isEmpty(param.getPassword())) {
            param.setPassword(null);
        }
        boolean isInsert = param.getId() == null || param.getId() == 0;
        SysUser user = BeanUtil.copyProperties(param, SysUser.class);
        if(isInsert && StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(PermissionConstant.DEFAULT_PASSWORD);
        }
        // 默认账号不允许修改
        if(Objects.equals(user.getId(), PermissionConstant.DEFAULT_ADMIN_USERID)) {
            user.setUsername(PermissionConstant.DEFAULT_ADMIN_USERNAME);
            user.setUserScope(UserScopeEnum.ADMIN);
            user.setUserStatus(UserStatusEnum.ENABLE);
        }
        if(StringUtils.isNotEmpty(user.getPassword())) {
            String originalPassword = user.getPassword();
            initPassword(user, originalPassword);
        }
        if (isInsert) {
            userDao.insert(user);
        } else {
            userDao.updateById(user);
        }
        Long userId = user.getId();
        userRoleService.updateByUserId(userId, param.getRoleIds());
        userCache.clear(userId);
        return userDao.findById(userId);
    }

    @Override
    public List<Long> getRoleIds(Long id) {
        List<SysRole> roleList = userRoleService.getByUserId(id);
        return roleList.stream().map(SysRole::getId).toList();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        if(Objects.equals(id, PermissionConstant.DEFAULT_ADMIN_USERID)) {
            throw new ClientException("默认管理账号不允许删除", FrameworkErrorCode.NOT_ALLOW);
        }
        userDao.deleteById(id);
        userRoleService.updateByUserId(id, null);
        userCache.clear(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updatePassword(Long userId, SysUserUpdatePasswordDTO param) {
        SysUser getObj = userDao.selectById(userId);
        if(getObj == null) {
            throw new ClientException("用户不存在", FrameworkErrorCode.RESOURCE_NOT_FOUND);
        }
        String oldPassword = SaSecureUtil.md5BySalt(param.getOldPassword(), getObj.getPasswordSalt());
        if(!Objects.equals(oldPassword, getObj.getPassword())) {
            throw new ClientException("旧密码不正确", FrameworkErrorCode.NOT_ALLOW);
        }
        String originalPassword = param.getNewPassword();
        initPassword(getObj, originalPassword);
        userDao.updateById(getObj);
        userCache.clear(userId);
    }

    @Override
    public void resetPassword(SysUser sysUser, String newPassword) {
        initPassword(sysUser, newPassword);
        userDao.updateById(sysUser);
        userCache.clear(sysUser.getId());
    }

    private void initPassword(SysUser user, String originalPassword) {
        String passwordSalt = UUID.fastUUID().toString(true);
        String password = SaSecureUtil.md5BySalt(originalPassword, passwordSalt);
        user.setPasswordSalt(passwordSalt);
        user.setPassword(password);
    }
}
