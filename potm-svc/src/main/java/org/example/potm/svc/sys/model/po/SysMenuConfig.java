package org.example.potm.svc.sys.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.potm.framework.pojo.PO;

@Data
@TableName("sys_menu_config")
public class SysMenuConfig implements PO {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String menuJson;
    private String adminAsyncRoutesJson;
}
