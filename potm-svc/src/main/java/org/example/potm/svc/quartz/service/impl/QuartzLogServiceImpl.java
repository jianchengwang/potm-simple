package org.example.potm.svc.quartz.service.impl;

import org.example.potm.svc.quartz.dao.QuartzLogDao;
import org.example.potm.svc.quartz.model.po.QuartzLog;
import org.example.potm.svc.quartz.service.QuartzLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("quartzLogService")
@Slf4j
@RequiredArgsConstructor
public class QuartzLogServiceImpl implements QuartzLogService {

    private final QuartzLogDao quartzLogDao;

    @Override
    public Integer insert(QuartzLog quartzLog) {
        return quartzLogDao.insert(quartzLog);
    }
}
