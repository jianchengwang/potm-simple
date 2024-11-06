package org.example.potm.svc.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.sys.dao.SysNotifyDao;
import org.example.potm.svc.sys.model.po.SysNotify;
import org.example.potm.svc.sys.model.query.SysNotifyQuery;
import org.example.potm.svc.sys.model.vo.SysNotifyVO;
import org.example.potm.svc.sys.service.SysNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author jianchengwang
 * @since 2023-12-26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysNotifyServiceImpl extends ServiceImpl<SysNotifyDao, SysNotify> implements SysNotifyService {
    @Override
    public IPage<SysNotifyVO> page(PageInfo pageInfo, SysNotifyQuery param) {
        LambdaQueryWrapper<SysNotify> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, SysNotify.class));
        return baseMapper.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    @Override
    public void batchInsert(List<SysNotify> insertList) {
        this.saveBatch(insertList);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Serializable id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void batchRead(Long userId, List<String> idList) {
        LambdaQueryWrapper<SysNotify> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysNotify::getUserId, userId);
        queryWrapper.in(SysNotify::getId, idList);
        SysNotify updateObj = new SysNotify();
        updateObj.setIsRead((short) 1);
        baseMapper.update(updateObj, queryWrapper);
    }

    @Override
    public void batchDelete(Long userId, List<String> idList) {
        LambdaQueryWrapper<SysNotify> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysNotify::getUserId, userId);
        queryWrapper.in(SysNotify::getId, idList);
        baseMapper.delete(queryWrapper);
    }
}

