package org.example.potm.framework.config.dict;

import java.util.List;

public enum DictKeyEnum {
    // default
    file_module("file_module", "文件存储模块", "default", true, true, 0, null),
    user_scope("user_scope", "系统用户归属", "default", true, true, 0, null),
    user_status("user_status", "系统用户状态", "default", true, true, 0, null),
    quartz_job_status("quartz_job_status", "调度任务状态", "default", true, true, 1, null),
    quartz_log_status("quartz_log_status", "调度任务日志状态", "default", true, true, 1, null),
    ;

    DictKeyEnum(String key, String description, String svc, Boolean systemFlag, Boolean enumFlag, Integer sortOrder, List<SysDictItem> itemList) {
        this.key = key;
        this.description = description;
        this.remark = description;
        this.svc = svc;
        this.systemFlag = systemFlag;
        this.enumFlag = enumFlag;
        this.sortOrder = sortOrder;
        this.itemList = itemList;
    }

    DictKeyEnum(String key, String description, String remark, String svc, Boolean systemFlag, Boolean enumFlag, Integer sortOrder, List<SysDictItem> itemList) {
        this.key = key;
        this.description = description;
        this.remark = remark;
        this.svc = svc;
        this.systemFlag = systemFlag;
        this.enumFlag = enumFlag;
        this.sortOrder = sortOrder;
        this.itemList = itemList;
    }

    private String key;
    private String description;
    private String remark;
    private String svc;
    private Boolean systemFlag;
    private Boolean enumFlag;
    private Integer sortOrder;
    private List<SysDictItem> itemList;

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getSvc() {
        return this.svc;
    }

    public Boolean getSystemFlag() {
        return this.systemFlag;
    }

    public Boolean getEnumFlag() {
        return this.enumFlag;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public List<SysDictItem> getItemList() {
        return this.itemList;
    }
}
