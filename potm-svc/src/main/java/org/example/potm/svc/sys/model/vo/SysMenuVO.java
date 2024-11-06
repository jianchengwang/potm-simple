package org.example.potm.svc.sys.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.VO;

@Data
@Schema(description = "管理端-系统模块-菜单-VO")
public class SysMenuVO implements VO {
    @Schema(description = "编号")
    private String id;
    @Schema(description = "上级菜单编号")
    private String parentId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "权限")
    private String permissions;
    @Schema(description = "按钮标识")
    private Boolean buttonFlag;
    @Schema(description = "路由名称")
    private String routeName;
    @Schema(description = "路由路径")
    private String routePath;
    @Schema(description = "路由图标")
    private String routeMetaIcon;
    @Schema(description = "路由缓存")
    private Boolean routeMetaKeepAlive;
    @Schema(description = "路由内嵌页面地址")
    private String routeMetaFrameSrc;
    @Schema(description = "来源类型")
    private String sourceType;
    @Schema(description = "隐藏")
    private Boolean hidden;
    @Schema(description = "排序")
    private Integer sortOrder;
    @Schema(description = "是否在菜单显示")
    private Boolean showLink;
    @Schema(description = "激活路径")
    private String activePath;
}
