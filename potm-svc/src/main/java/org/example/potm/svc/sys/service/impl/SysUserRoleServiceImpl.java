package org.example.potm.svc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.example.potm.svc.sys.dao.SysUserRoleDao;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.po.SysUserRole;
import org.example.potm.svc.sys.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.potm.framework.config.permission.user.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

    @Override
    public List<SysRole> getByUserId(Long userId) {
        return baseMapper.getByUserId(userId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateByUserId(Long userId, List<Long> roleIdList) {
        LambdaQueryWrapper<SysUserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysUserRole::getUserId, userId);
        baseMapper.delete(deleteWrapper);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return;
        }
        roleIdList.removeIf(Objects::isNull);
        List<SysUserRole> insertList = Lists.newArrayList();
        roleIdList.stream().distinct().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(insertList::add);
        this.saveBatch(insertList);
    }

    @Override
    public List<SysUser> getByRoleId(Long roleId) {
        return baseMapper.getByRoleId(roleId);
    }
}
