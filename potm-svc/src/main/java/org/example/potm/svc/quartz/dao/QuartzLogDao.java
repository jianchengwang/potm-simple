package org.example.potm.svc.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.potm.svc.quartz.model.po.QuartzLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuartzLogDao extends BaseMapper<QuartzLog> {
}
