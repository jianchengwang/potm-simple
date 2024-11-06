package org.example.potm.svc.sys.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 机构信息
 * </p>
 *
 * @author sonin
 * @since 2024-08-22
 */
@Data
public class SysDepartInfo {

    /**
     * 主键
     */
    private String id;

    /**
     * 机构名称
     */
    private String departName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 层级
     */
    private String hierarchy;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 0展开；1收起
     */
    private String defaultExpandVal;

    /**
     * 0展开；1收起
     */
    private String defaultExpandVal2;


    /**
     * 0未付费；1付费
     */
    private String payFlag;


}
