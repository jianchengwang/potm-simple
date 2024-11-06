package org.example.potm.svc.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.storage.dao.StorageFileDao;
import org.example.potm.svc.storage.model.po.StorageFile;
import org.example.potm.svc.storage.service.StorageFileService;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.plugins.storage.adapter.StorageAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StorageFileServiceImpl extends ServiceImpl<StorageFileDao, StorageFile> implements StorageFileService {

    private final StorageAdapter storageAdapter;

    public StorageFileServiceImpl(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean removeById(Serializable id) {
        boolean flag = super.removeByIds(Collections.singletonList(id));
        if(flag) {
            StorageFile file = baseMapper.selectById(id);
            storageAdapter.doRemove(file.getPath());
        }
        return flag;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean removeByIds(Collection<?> idList) {
        if(CollectionUtils.isNotEmpty(idList)) {
            LambdaQueryWrapper<StorageFile> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(StorageFile::getId, idList);
            Collection<StorageFile> fileList = baseMapper.selectList(queryWrapper);
            boolean flag = super.removeByIds(idList);
            if(flag) {
                List<String> keyList = fileList.stream().map(StorageFile::getPath).collect(Collectors.toList());
                storageAdapter.doBatchRemove(keyList);
            }
            return flag;
        }
        return true;
    }
}
