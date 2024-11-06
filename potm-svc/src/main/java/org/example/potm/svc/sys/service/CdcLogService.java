package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.CdcLogQuery;
import org.example.potm.svc.sys.model.vo.CdcLogInfoVO;
import org.example.potm.svc.sys.model.vo.CdcLogRowDetailVO;
import org.example.potm.framework.pojo.PageInfo;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/9
 */
public interface CdcLogService {

    IPage<CdcLogInfoVO> page(PageInfo pageInfo, CdcLogQuery param);

    List<CdcLogRowDetailVO> getCdcLogRowDetails(Long logInfoId);

    void deleteByIds(String ids);
}
