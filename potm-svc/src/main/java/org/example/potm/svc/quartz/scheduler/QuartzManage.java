package org.example.potm.svc.quartz.scheduler;

import org.example.potm.svc.quartz.constant.QuartzConstant;
import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import org.example.potm.svc.quartz.model.po.QuartzJob;
import org.example.potm.svc.quartz.scheduler.QuartzRecord;
import jakarta.annotation.Resource;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
public class QuartzManage {

    private static final String SCHEDULE_NAME = "BIZ_JOB_" ;

    @Resource
    private Scheduler scheduler ;

    /**
     * 触发器 KEY
     */
    public TriggerKey getTriggerKey(Long jobId){
        return TriggerKey.triggerKey(SCHEDULE_NAME+jobId) ;
    }

    /**
     * 定时任务 Key
     */
    public JobKey getJobKey (Long jobId){
        return JobKey.jobKey(SCHEDULE_NAME+jobId) ;
    }

    /**
     * 表达式触发器
     */
    public CronTrigger getCronTrigger (Long jobId){
        try {
            return (CronTrigger) this.scheduler.getTrigger(getTriggerKey(jobId)) ;
        } catch (SchedulerException e){
            throw new RuntimeException("getCronTrigger Fail",e) ;
        }
    }

    /**
     * 创建定时器
     */
    public void createJob (QuartzJob quartzJob){
        try {
            // 构建任务
            JobDetail jobDetail = JobBuilder.newJob(QuartzRecord.class)
                    .withIdentity(getJobKey(quartzJob.getId())).build() ;

            // 构建Cron调度器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(quartzJob.getCronExpres())
                    .withMisfireHandlingInstructionDoNothing() ;
            // 任务触发器
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(quartzJob.getId()))
                    .withSchedule(scheduleBuilder).build() ;
            jobDetail.getJobDataMap().put(QuartzConstant.JOB_PARAM_KEY,quartzJob);
            scheduler.scheduleJob(jobDetail,trigger) ;
            // 状态校验
            checkStop(quartzJob) ;
        } catch (SchedulerException e){
            throw new RuntimeException("createJob Fail",e) ;
        }
    }

    /**
     * 更新定时任务
     */
    public void updateJob(QuartzJob quartzJob) {
        try {
            // 查询触发器Key
            TriggerKey triggerKey = getTriggerKey(quartzJob.getId());
            // 构建Cron调度器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(quartzJob.getCronExpres())
                    .withMisfireHandlingInstructionDoNothing();
            // 任务触发器
            CronTrigger trigger = getCronTrigger(quartzJob.getId())
                    .getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put(QuartzConstant.JOB_PARAM_KEY, quartzJob);
            scheduler.rescheduleJob(triggerKey, trigger);
            // 状态校验
            checkStop(quartzJob) ;
        } catch (SchedulerException e) {
            throw new RuntimeException("updateJob Fail",e) ;
        }
    }

    /**
     * 恢复定时器
     */
    public void resumeJob (Long jobId){
        try {
            this.scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e){
            throw new RuntimeException("resumeJob Fail",e) ;
        }
    }

    /**
     * 删除定时器
     */
    public void deleteJob (Long jobId){
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e){
            throw new RuntimeException("deleteJob Fail",e) ;
        }
    }

    /**
     * 执行定时器
     */
    public void run (QuartzJob quartzJob){
        try {
            JobDataMap dataMap = new JobDataMap() ;
            dataMap.put(QuartzConstant.JOB_PARAM_KEY,quartzJob);
            this.scheduler.triggerJob(getJobKey(quartzJob.getId()),dataMap);
        } catch (SchedulerException e){
            throw new RuntimeException("run Fail",e) ;
        }
    }

    /**
     * 校验停止定时器
     */
    public void checkStop (QuartzJob quartzJob){
        try {
            if(quartzJob.getQuartzJobStatus() != QuartzJobStatusEnum.RUNNING){
                this.scheduler.pauseJob(getJobKey(quartzJob.getId()));
            }
        } catch (SchedulerException e){
            throw new RuntimeException("pauseJob Fail",e) ;
        }
    }

    public boolean checkCron(String cronExpres) {
        try {
            new CronExpression(cronExpres);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
