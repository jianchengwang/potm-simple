package org.example.potm.svc.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.sys.dao.SysDatasourceMapper;
import org.example.potm.svc.sys.model.po.SysDatasource;
import org.example.potm.svc.sys.service.SysDatasourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 动态数据源表 服务实现类
 * </p>
 *
 * @author sonin
 * @since 2024-09-19
 */
@Service
public class SysDatasourceServiceImpl extends ServiceImpl<SysDatasourceMapper, SysDatasource> implements SysDatasourceService {
}
