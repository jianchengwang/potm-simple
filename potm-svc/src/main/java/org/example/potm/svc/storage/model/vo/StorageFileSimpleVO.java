package org.example.potm.svc.storage.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.potm.framework.pojo.VO;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "存储对象-VO")
public class StorageFileSimpleVO implements VO {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "原始文件名")
    private String originalFileName;
    @Schema(description = "mime类型")
    private String mimeType;
    @Schema(description = "文件后缀名")
    private String suffix;
    @Schema(description = "对象大小")
    private Long size;
    @Schema(description = "存储对象key")
    private String path;
}
