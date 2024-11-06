package org.example.potm.svc.sys.model.dto;

import org.example.potm.svc.sys.model.po.SysDatasource;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author：sonin
 * @Date：2024/9/19 10:11
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysDatasourceDTO extends SysDatasource {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

    private String startTime;

    private String endTime;

    private String versionNo;

    private String versionContent;

}
