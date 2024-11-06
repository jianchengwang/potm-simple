package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.CdcLogQuery;
import org.example.potm.svc.sys.model.vo.CdcLogInfoVO;
import org.example.potm.svc.sys.model.vo.CdcLogRowDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.example.potm.framework.config.cdc.CdcLogInfo;

import java.util.List;

@Mapper
public interface CdcLogDao {
    IPage<CdcLogInfoVO> page(IPage<CdcLogInfo> page, CdcLogQuery param, LambdaQueryWrapper<CdcLogInfo> ew);
    List<CdcLogRowDetailVO> getCdcLogRowDetails(Long logInfoId);

    void deleteBatchIds(List<String> idList);
}
