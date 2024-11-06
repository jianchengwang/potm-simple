package org.example.potm.svc.sys.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.potm.framework.pojo.PO;

@Data
@TableName("sys_role")
public class SysRole implements PO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String roleCode;
    private String roleName;
    private String remark;
}
