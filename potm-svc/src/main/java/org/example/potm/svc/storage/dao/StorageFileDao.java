package org.example.potm.svc.storage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.potm.svc.storage.model.po.StorageFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageFileDao extends BaseMapper<StorageFile> {
}
