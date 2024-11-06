package org.example.potm.svc.sys.model.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 动态数据源表
 * </p>
 *
 * @author sonin
 * @since 2024-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDatasource {

    /**
     * 主键
     */
    private String id;

    /**
     * 数据源名称
     */
    private String datasource;

    /**
     * 数据源别名
     */
    private String sourceName;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动名称
     */
    private String driverClassName;

    /**
     * 数据库地址
     */
    private String url;

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


}
