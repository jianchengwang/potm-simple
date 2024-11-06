package org.example.potm.svc.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.DTO;

@Schema(description = "管理端-系统菜单配置-DTO")
@Data
public class SysMenuConfigSaveDTO implements DTO {
    @Schema(description = "菜单配置")
    private String menuJson;
    @Schema(description = "管理平台动态路由")
    private String adminAsyncRoutesJson;
}
