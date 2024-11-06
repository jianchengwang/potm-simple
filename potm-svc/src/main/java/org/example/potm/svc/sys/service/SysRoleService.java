package org.example.potm.svc.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.dto.SysRoleSaveDTO;
import org.example.potm.svc.sys.model.po.SysRole;
import org.example.potm.svc.sys.model.query.SysRoleQuery;
import org.example.potm.svc.sys.model.vo.SysRoleVO;
import org.example.potm.framework.pojo.PageInfo;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/11/8
 */
public interface SysRoleService {

    IPage<SysRoleVO> page(PageInfo pageInfo, SysRoleQuery param);

    List<SysRoleVO> fetchAll();

    SysRole findById(Long id);

    void save(SysRoleSaveDTO param);

    void deleteById(Long id);

    List<String> getMenuIds(Long id);
}
