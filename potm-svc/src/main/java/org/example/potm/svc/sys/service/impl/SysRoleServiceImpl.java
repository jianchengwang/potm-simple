package org.example.potm.svc.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.sys.cache.SysUserCache;
import org.example.potm.svc.sys.dao.SysRoleDao;
import org.example.potm.svc.sys.model.dto.SysRoleSaveDTO;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.query.SysRoleQuery;
import org.example.potm.svc.sys.model.vo.SysRoleVO;
import org.example.potm.svc.sys.service.SysRoleMenuService;
import org.example.potm.svc.sys.service.SysRoleService;
import org.example.potm.svc.sys.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.config.permission.user.SysUser;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    private final SysRoleMenuService roleMenuService;
    private final SysUserRoleService userRoleService;
    private final SysUserCache userCache;

    public IPage<SysRoleVO> page(PageInfo pageInfo, SysRoleQuery param) {
        LambdaQueryWrapper<SysRole> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, SysRole.class));
        return baseMapper.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    @Override
    public List<SysRoleVO> fetchAll() {
        return baseMapper.fetchAll();
    }

    @Override
    public SysRole findById(Long id) {
        SysRole getObj = baseMapper.selectById(id);
        if(getObj == null) {
            throw new ClientException("角色不存在", FrameworkErrorCode.RESOURCE_NOT_FOUND);
        }
        return getObj;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(SysRoleSaveDTO param) {
        SysRole role = BeanUtil.copyProperties(param, SysRole.class);
        // 校验角色编码
        if(checkRoleCodeExisted(role.getRoleCode(), role.getId())) {
            throw new ClientException("角色标识已存在", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        if(role.getId() == null || role.getId() == 0) {
            role.setId(null);
            baseMapper.insert(role);
        } else {
            baseMapper.updateById(role);
        }
        // 更新菜单权限
        Long roleId = role.getId();
        roleMenuService.updateByRoleId(roleId, param.getMenuIds());
        // 清除关联用户缓存
        List<SysUser> userList = userRoleService.getByRoleId(roleId);
        if(CollectionUtils.isEmpty(userList)) {
            return;
        }
        userList.forEach(user -> userCache.clear(user.getId()));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        // 关联用户不允许删除
        List<SysUser> userList = userRoleService.getByRoleId(id);
        if(CollectionUtils.isNotEmpty(userList)) {
            throw new ClientException("该角色下存在用户，不允许删除", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }
        // 删除权限关联数据
        roleMenuService.deleteByRoleIdList(Collections.singletonList(id));
        baseMapper.deleteById(id);
    }

    @Override
    public List<String> getMenuIds(Long id) {
        return roleMenuService.getByRoleIdList(Collections.singletonList(id));
    }

    private boolean checkRoleCodeExisted(String roleCode, Long id) {
        if(StringUtils.isEmpty(roleCode)) {
            return false;
        }
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRole::getRoleCode, roleCode);
        if(id != null) {
            queryWrapper.ne(SysRole::getId, id);
        }
        return baseMapper.selectCount(queryWrapper) > 0;
    }
}
