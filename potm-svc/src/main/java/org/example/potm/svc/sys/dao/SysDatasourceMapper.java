package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.potm.svc.sys.model.po.SysDatasource;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 动态数据源表 Mapper 接口
 * </p>
 *
 * @author sonin
 * @since 2024-09-19
 */
@Mapper
public interface SysDatasourceMapper extends BaseMapper<SysDatasource> {

}
