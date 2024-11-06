package org.example.potm.svc.sys.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.potm.framework.pojo.PO;

@Data
@TableName("sys_menu")
public class SysMenu implements PO {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    private String parentId;
    private String name;
    private String code;
    private String permissions;
    private Boolean buttonFlag;
    private String routeName;
    private String routePath;
    private String routeMetaIcon;
    private Boolean routeMetaKeepAlive;
    private String routeMetaFrameSrc;
    private String sourceType;
    private Boolean hidden;
    private Integer sortOrder;
    private Boolean showLink;
    private String activePath;
}
