package org.example.potm.svc.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.quartz.model.dto.QuartzJobSaveDTO;
import org.example.potm.svc.quartz.model.query.QuartzJobQuery;
import org.example.potm.svc.quartz.model.vo.QuartzJobVO;
import org.example.potm.framework.pojo.PageInfo;

public interface QuartzJobService {
    IPage<QuartzJobVO> page(PageInfo pageInfo, QuartzJobQuery param);
    QuartzJobVO getById(Long id);
    void saveOrUpdate(QuartzJobSaveDTO param);
    void pause(Long id);
    void resume(Long id);
    void runOnce(Long id);
}
