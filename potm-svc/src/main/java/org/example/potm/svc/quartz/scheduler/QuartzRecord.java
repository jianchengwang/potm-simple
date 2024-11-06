package org.example.potm.svc.quartz.scheduler;

import org.example.potm.svc.quartz.constant.QuartzConstant;
import org.example.potm.svc.quartz.constant.QuartzLogStatusEnum;
import org.example.potm.svc.quartz.model.po.QuartzJob;
import org.example.potm.svc.quartz.model.po.QuartzLog;
import org.example.potm.svc.quartz.service.QuartzLogService;
import org.example.potm.framework.utils.ApplicationContextUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class QuartzRecord extends QuartzJobBean {

    @Value("${app.quartz.enable:true}")
    private boolean quartzEnable;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        if (!quartzEnable) {
            System.out.println("定时任务已关闭");
            return;
        }

        QuartzJob quartzJob = (QuartzJob)context.getMergedJobDataMap().get(QuartzConstant.JOB_PARAM_KEY) ;
        QuartzLogService quartzLogService = (QuartzLogService) ApplicationContextUtils.getBean("quartzLogService") ;
        // 定时器日志记录
        QuartzLog quartzLog = new QuartzLog () ;
        quartzLog.setJobId(quartzJob.getId());
        quartzLog.setBeanName(quartzJob.getBeanName());
        quartzLog.setParams(quartzJob.getParams());
        quartzLog.setCreateTime(LocalDateTime.now());
        long beginTime = System.currentTimeMillis() ;
        try {
            // 加载并执行
            Object target = ApplicationContextUtils.getBean(quartzJob.getBeanName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, quartzJob.getParams());
            long executeTime = System.currentTimeMillis() - beginTime;
            quartzLog.setTimes((int)executeTime);
            quartzLog.setQuartzLogStatus(QuartzLogStatusEnum.SUCCESS);
        } catch (Exception e){
            // 异常信息
            long executeTime = System.currentTimeMillis() - beginTime;
            quartzLog.setTimes((int)executeTime);
            quartzLog.setQuartzLogStatus(QuartzLogStatusEnum.FAIL);
            quartzLog.setError(e.getMessage());
        } finally {
            // 保存执行日志
            quartzLogService.insert(quartzLog) ;
        }
    }
}
