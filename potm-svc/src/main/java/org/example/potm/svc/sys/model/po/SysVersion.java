package org.example.potm.svc.sys.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 系统版本管理
 * </p>
 *
 * @author sonin
 * @since 2024-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysVersion {

    /**
     * 主键
     */
    private String id;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本更新内容
     */
    private String versionContent;

    /**
     * 版本标识ID
     */
    private String versionId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 排序字段
     */
    private Integer orderNum;

    /**
     * 删除状态(0正常;1已删除)
     */
    private String delFlag;

    private String sysType;

}
