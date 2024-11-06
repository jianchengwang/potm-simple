package org.example.potm.svc.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.dao.CdcLogDao;
import org.example.potm.svc.sys.model.query.CdcLogQuery;
import org.example.potm.svc.sys.model.vo.CdcLogInfoVO;
import org.example.potm.svc.sys.model.vo.CdcLogRowDetailVO;
import org.example.potm.svc.sys.service.CdcLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.config.cdc.CdcLogInfo;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianchengwang
 * @date 2023/4/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CdcLogServiceImpl implements CdcLogService {

    private final CdcLogDao cdcLogDao;

    public IPage<CdcLogInfoVO> page(PageInfo pageInfo, CdcLogQuery param) {
        LambdaQueryWrapper<CdcLogInfo> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, CdcLogInfo.class));
        if(StringUtils.isNotEmpty(param.getDataRange())) {
            MpHelper.dateBetween(ew, CdcLogInfo::getLogTime, param.getDataRange());
        }
        return cdcLogDao.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    public List<CdcLogRowDetailVO> getCdcLogRowDetails(Long logInfoId) {
        return cdcLogDao.getCdcLogRowDetails(logInfoId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteByIds(String ids) {
        if(StringUtils.isEmpty(ids)) {
            return;
        }
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(idList)) {
            return;
        }
        cdcLogDao.deleteBatchIds(idList);
    }
}
