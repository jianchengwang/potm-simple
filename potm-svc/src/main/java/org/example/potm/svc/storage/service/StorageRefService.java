package org.example.potm.svc.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.example.potm.svc.storage.model.po.StorageRef;
import org.example.potm.svc.storage.model.vo.StorageFileVO;

import java.util.List;
import java.util.Map;

public interface StorageRefService extends IService<StorageRef> {
    /**
     * 更新关联文件列表
     * @param refId 关联编号
     * @param fileModule 文件库模型
     * @param aq 附加条件
     * @return
     */
    List<StorageFileVO> getRefFileList(String refId, FileModuleEnum fileModule, Integer aq);

    /**
     * 更新关联文件列表
     * @param refIdList 关联编号集合
     * @param fileModule 文件库模型
     * @param aq 附加条件
     * @return
     */
    List<StorageFileVO> getRefFileList(List<String> refIdList, FileModuleEnum fileModule, Integer aq);

    /**
     * 更新关联文件列表
     * @param refId 关联编号
     * @param fileModule 文件库模型
     * @param aq 附加条件
     * @param fileIdList 文件编号列表
     * @param emptyExisted 清空已存在的
     */
    void updateRefFileList(String refId, FileModuleEnum fileModule, Integer aq, List<Long> fileIdList, boolean emptyExisted);

    /**
     * 批量更新关联文件
     * @param refMap
     * @param fileModule
     * @param aq
     * @param emptyExisted
     */
    void updateRefFileMap(Map<String, List<Long>> refMap, FileModuleEnum fileModule, Integer aq, boolean emptyExisted);

    /**
     * 移除关联文件列表
     * @param fileModule 文件库模型
     * @param aq 附加条件
     * @param refIds 关联编号列表
     * @param excludeFileIdList 排除文件编号
     */
    void removeByRefIds(FileModuleEnum fileModule, Integer aq, List<String> refIds, List<Long> excludeFileIdList);
}
