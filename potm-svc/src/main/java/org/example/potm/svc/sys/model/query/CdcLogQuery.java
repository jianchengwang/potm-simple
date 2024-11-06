package org.example.potm.svc.sys.model.query;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.example.potm.framework.pojo.Query;
import org.springdoc.core.annotations.ParameterObject;

/**
 * @author jianchengwang
 * @date 2023/4/9
 */
@Getter
@Setter
@ParameterObject
public class CdcLogQuery extends Query {

    @Parameter(description = "时间范围")
    private String dataRange;
}
