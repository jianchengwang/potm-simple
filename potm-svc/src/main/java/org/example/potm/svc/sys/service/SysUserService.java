package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.dto.SysUserSaveDTO;
import org.example.potm.svc.sys.model.dto.SysUserUpdatePasswordDTO;
import org.example.potm.svc.sys.model.query.SysUserQuery;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import org.example.potm.framework.config.permission.user.SysUser;
import org.example.potm.framework.pojo.PageInfo;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
public interface SysUserService {

    IPage<SysUserVO> page(PageInfo pageInfo, SysUserQuery param);

    List<SysUserVO> list(SysUserQuery param);

    SysUserVO getById(Long id, boolean forceLoadFromDb);

    SysUser findByUsername(String username);

    SysUserVO save(SysUserSaveDTO param);

    List<Long> getRoleIds(Long id);

    void deleteById(Long id);

    void updatePassword(Long id, SysUserUpdatePasswordDTO param);

    void resetPassword(SysUser sysUser, String newPassword);
}
