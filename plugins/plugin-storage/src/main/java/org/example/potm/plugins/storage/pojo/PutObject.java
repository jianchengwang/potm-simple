package org.example.potm.plugins.storage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutObject {
    private String originalFileName;
    private String key;
    private InputStream inputStream;
}
