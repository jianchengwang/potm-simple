package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.SysUserQuery;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.example.potm.framework.config.permission.user.SysUser;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/3/30
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {
    SysUser getByUsername(String username);

    IPage<SysUserVO> page(IPage<SysUser> page, SysUserQuery param, LambdaQueryWrapper<SysUser> ew);
    List<SysUserVO> list(SysUserQuery param, LambdaQueryWrapper<SysUser> ew);

    SysUserVO findById(Long id);
}
