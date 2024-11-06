package org.example.potm.plugins.storage.autoconfigure.properties;

import lombok.Data;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.exception.ServerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class FSProperties {
    private String rootPath = "/uploads";

    public void validate() {

        Path path = Paths.get(rootPath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new ServerException("Cannot create file adapter, create directory error: " + e.getMessage(), FrameworkErrorCode.INVALID_CONFIG, e);
            }
        }

        if ((rootPath==null || rootPath.isEmpty())
                || Files.notExists(path, LinkOption.NOFOLLOW_LINKS)
                || !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)
                || !Files.isExecutable(path)
                || !Files.isReadable(path)
                || !Files.isWritable(path)
        ) {
            throw new ServerException("Cannot create file adapter, because rootPath not found or rootPath is not directory or cant access, read or write", FrameworkErrorCode.INVALID_CONFIG, null);
        }

    }
}
