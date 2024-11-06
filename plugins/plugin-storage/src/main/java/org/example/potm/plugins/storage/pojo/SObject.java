package org.example.potm.plugins.storage.pojo;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
public class SObject {
    private String originalFileName; // 文件的原始名称
    private String dbId; // 如果有插入数据库对象，则存储到数据库存储对象表的编号
    private String name; // 文件名
    private String key; // 对象key
    private String hash; // hash
    private Long size; // 大小
    private Long lastModified; // 最后一次修改时间

    private String nameWithoutSuffix; // 没有后缀的文件名
    private String suffix; // 文件名后缀
    private String mimeType; // mime类型
    private InputStream inputStream; // 文件流

    public SObject(String key, String hash, Long size, Long lastModified, String mineType) {
        this.setKey(key);
        this.setHash(hash);
        this.setSize(size);
        this.setLastModified(lastModified);
        this.setMimeType(mineType);
    }

    public SObject(String key, String mimeType, String hash, Long size) {
        this.setKey(key);
        this.setMimeType(mimeType);
        this.setHash(hash);
        this.setSize(size);
    }

    public SObject(String key, String mimeType, InputStream inputStream) {
        this.setKey(key);
        this.setMimeType(mimeType);
        this.setInputStream(inputStream);
    }

    public SObject(Path fsObj, String fullPath) throws IOException {
        this(fullPath!=null?fullPath:fsObj.toString(), String.valueOf(fsObj.hashCode()),  Files.size(fsObj), Files.getLastModifiedTime(fsObj).toMillis(), null);
    }

    /**
     * 设置key的时候自动设置文件名,后缀
     */
    public void setKey(String key) {
        this.key = key;
        String name;
        if(key.contains(File.pathSeparator)) {
            name = key.substring(key.lastIndexOf(File.separator) + 1);
        } else {
            name = key.substring(key.lastIndexOf("/") + 1);
        }
        this.setName(name);
        if(name.lastIndexOf(".") != -1) {
            String suffix = name.substring(name.lastIndexOf("."));
            this.setSuffix(suffix);
            this.setNameWithoutSuffix(name.substring(0, name.length() - suffix.length()));
        }
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileName() {
        if(StrUtil.isNotEmpty(originalFileName)) {
            return originalFileName;
        }
        return name;
    }
}
