package org.example.potm.plugins.storage.adapter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.exception.ServerException;
import org.example.potm.plugins.storage.autoconfigure.properties.FSProperties;
import org.example.potm.plugins.storage.autoconfigure.properties.StoreType;
import org.example.potm.plugins.storage.constant.StorageErrorCode;
import org.example.potm.plugins.storage.pojo.SObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Slf4j
@Data
public class FSAdapter implements StorageAdapter {

    private final FSProperties CONFIG;
    private final String rootPath;

    public FSAdapter(final FSProperties config) {
        CONFIG = config;
        rootPath = CONFIG.getRootPath();
    }

    @Override
    public StoreType getStoreType() {
        return StoreType.fs;
    }

    @Override
    public SObject doPut(String key, InputStream inputStream, boolean forcedPut) {
        try {
            Path path = getRootFullPath(key);
            if(!forcedPut) {
                if(Files.exists(path)) {
                    throw new ClientException(StorageErrorCode.PUT_OBJECT_KEY_EXISTED, key);
                }
            }
            Path dir = Paths.get(path.toString().substring(0, path.toString().lastIndexOf(File.separator)));
            if(Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            if(Files.notExists(path)) {
                Files.createFile(path);
            }
            byte[] buff = toByteArray(inputStream);
            String mimeType = Files.probeContentType(path);
            Files.write(path, buff, StandardOpenOption.CREATE);
            return new SObject(key, mimeType, null, (long) buff.length);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerException("put storage object error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    private byte[] toByteArray(InputStream inputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ServerException("inputStream to byte[] error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    @Override
    public SObject doGet(String key) {
        try {
            key = key.replace(File.separatorChar, '/');
            Path path = getRootFullPath(key);
            if(Files.notExists(path)) {
                throw new ClientException(StorageErrorCode.OBJECT_KEY_NOT_EXIST, key);
            }
            String mimeType = Files.probeContentType(path);
            byte[] bytes = Files.readAllBytes(path);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return new SObject(key, mimeType, inputStream);
        } catch (ClientException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServerException("get storage object error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    @Override
    public void doRemove(String key) {
        try {
            Path path = getRootFullPath(key);
            Files.deleteIfExists(path);
        } catch (Exception e) {
            throw new ServerException("remove storage object error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    @Override
    public void doBatchRemove(List<String> keyList) {
        try {
            keyList.forEach(this::doRemove);
        } catch (Exception e) {
            throw new ServerException("remove storage object error", FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    private Path getRootFullPath(String... fullPath) {
        return Paths.get(rootPath, fullPath);
    }

    private String getFullPath(Path path) {
        String fullPath = path.toString().replace(rootPath, "");
        if(fullPath.startsWith(File.separator)) {
            fullPath = fullPath.substring(File.separator.length());
        }
        return fullPath.replace(File.separator, "/");
    }
}
