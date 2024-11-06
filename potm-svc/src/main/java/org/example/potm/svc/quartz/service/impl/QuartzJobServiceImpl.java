package org.example.potm.svc.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import org.example.potm.svc.quartz.dao.QuartzJobDao;
import org.example.potm.svc.quartz.job.JobService;
import org.example.potm.svc.quartz.model.dto.QuartzJobSaveDTO;
import org.example.potm.svc.quartz.model.po.QuartzJob;
import org.example.potm.svc.quartz.model.query.QuartzJobQuery;
import org.example.potm.svc.quartz.model.vo.QuartzJobVO;
import org.example.potm.svc.quartz.scheduler.QuartzManage;
import org.example.potm.svc.quartz.service.QuartzJobService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.config.mybatis.MpHelper;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.PojoConverter;
import org.example.potm.framework.utils.ApplicationContextUtils;
import org.example.potm.framework.utils.PageUtils;
import org.quartz.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("quartzJobService")
@Slf4j
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {

    private final QuartzJobDao baseMapper;
    
    private final QuartzManage quartzManage;

    /**
     * 初始化加载定时任务
     */
    @PostConstruct
    public void init () {
        LambdaQueryWrapper<QuartzJob> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.in(QuartzJob::getQuartzJobStatus, QuartzJobStatusEnum.RUNNING, QuartzJobStatusEnum.STOP);
        List<QuartzJob> jobList = baseMapper.selectList(queryWrapper);
        jobList.forEach(quartzJob -> {
            CronTrigger cronTrigger = quartzManage.getCronTrigger(quartzJob.getId()) ;
            if (Objects.isNull(cronTrigger)){
                quartzManage.createJob(quartzJob);
            } else {
                quartzManage.updateJob(quartzJob);
            }
        });
    }

    @Override
    public IPage<QuartzJobVO> page(PageInfo pageInfo, QuartzJobQuery param) {
        LambdaQueryWrapper<QuartzJob> ew = MpHelper.lambdaQuery("a", BeanUtil.copyProperties(param, QuartzJob.class));
        return baseMapper.page(PageUtils.buildPage(pageInfo), param, ew);
    }

    @Override
    public QuartzJobVO getById(Long id) {
        QuartzJob getObj = baseMapper.selectById(id);
        if(getObj == null) {
            throw new ClientException("定时任务不存在", FrameworkErrorCode.RESOURCE_NOT_FOUND, id);
        }
        return PojoConverter.convert(getObj, QuartzJobVO.class);
    }

    @Override
    public void saveOrUpdate(QuartzJobSaveDTO param) {
        QuartzJob quartzJob = PojoConverter.convert(param, QuartzJob.class);
        // 校验任务处理类
        try {
            ApplicationContextUtils.getBean(quartzJob.getBeanName(), JobService.class);
        } catch (Exception e) {
            throw new ClientException("任务处理类不存在", FrameworkErrorCode.PARAM_VALIDATE_ERROR, quartzJob.getBeanName());
        }
        // 校验corn表达式
        boolean checkCron = quartzManage.checkCron(quartzJob.getCronExpres());
        if(!checkCron) {
            throw new ClientException("corn表达式校验失败", FrameworkErrorCode.PARAM_VALIDATE_ERROR, quartzJob.getCronExpres());
        }
        if(quartzJob.getId()!=null && quartzJob.getId()>0) {
            int flag = baseMapper.updateById(quartzJob);
            if (flag > 0){
                quartzManage.updateJob(quartzJob);
            }
        } else {
            int flag = baseMapper.insert(quartzJob);
            if (flag > 0){
                quartzManage.createJob(quartzJob) ;
            }
        }
    }

    @Override
    public void pause(Long id) {
        QuartzJob quartzJob = baseMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob)){
            quartzJob.setQuartzJobStatus(QuartzJobStatusEnum.STOP);
            if (baseMapper.updateById(quartzJob)>0){
                quartzManage.checkStop(quartzJob);
            }
        }
    }

    @Override
    public void resume(Long id) {
        QuartzJob quartzJob = baseMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob)){
            quartzJob.setQuartzJobStatus(QuartzJobStatusEnum.RUNNING);
            if (baseMapper.updateById(quartzJob)>0){
                quartzManage.resumeJob(id);
            }
        }
    }

    @Override
    public void runOnce(Long id) {
        QuartzJob quartzJob = baseMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob)){
            quartzManage.run(quartzJob);
        }
    }
}
