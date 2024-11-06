package org.example.potm.svc.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.potm.svc.sys.model.po.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenu> {

    @Update("truncate table sys_menu")
    void truncateMenuTable();
}
