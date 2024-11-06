package org.example.potm.svc.storage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.potm.svc.storage.model.po.StorageRef;
import org.example.potm.svc.storage.model.vo.StorageFileVO;

import java.util.List;

@Mapper
public interface StorageRefDao extends BaseMapper<StorageRef> {
    /**
     * 获取关联文件列表
     * @param refIdList
     * @param fileModule
     * @return
     */
    List<StorageFileVO> selectFileList(@Param("refIdList") List<String> refIdList, @Param("module") String fileModule, @Param("aq") Integer aq);

    /**
     * 筛选重复的文件记录，避免关联多条记录
     * @param refId
     * @param fileIdList
     * @return
     */
    List<Long> repeatRefFileIdList(@Param("refId") String refId, @Param("fileIdList") List<Long> fileIdList);
}
