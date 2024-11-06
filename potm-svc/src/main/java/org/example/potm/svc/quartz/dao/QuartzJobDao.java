package org.example.potm.svc.quartz.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.quartz.model.po.QuartzJob;
import org.example.potm.svc.quartz.model.query.QuartzJobQuery;
import org.example.potm.svc.quartz.model.vo.QuartzJobVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuartzJobDao extends BaseMapper<QuartzJob> {
    IPage<QuartzJobVO> page(IPage<Object> iPage, QuartzJobQuery param, LambdaQueryWrapper<QuartzJob> ew);
}
