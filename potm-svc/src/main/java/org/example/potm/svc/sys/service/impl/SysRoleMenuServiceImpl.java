package org.example.potm.svc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.example.potm.svc.sys.dao.SysRoleMenuDao;
import org.example.potm.svc.sys.model.po.SysRoleMenu;
import org.example.potm.svc.sys.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {
    @Override
    public List<String> getByRoleIdList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        return baseMapper.selectList(queryWrapper).stream().map(SysRoleMenu::getMenuId).distinct().collect(java.util.stream.Collectors.toList());
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateByRoleId(Long roleId, List<String> menuIdList) {
        this.deleteByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(menuIdList)) {
            return;
        }
        menuIdList.removeIf(Objects::isNull);
        List<SysRoleMenu> insertList = Lists.newArrayList();
        menuIdList.stream().distinct().map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            return roleMenu;
        }).forEach(insertList::add);
        this.saveBatch(insertList);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteByRoleIdList(List<Long> roleIdList) {
        if(CollectionUtils.isEmpty(roleIdList)) {
            return;
        }
        LambdaQueryWrapper<SysRoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        baseMapper.delete(deleteWrapper);
    }
}
