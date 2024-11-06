package org.example.potm.plugins.push.mail.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@Builder
public class AttachObject {
    private File file;
    private String fileName;

    public AttachObject(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
        if(this.fileName == null) {
            String filePath = file.getPath();
            this.fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        }
    }
}