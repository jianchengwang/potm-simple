package org.example.potm.framework.pojo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

/**
 * @author jianchengwang
 * @date 2023/4/13
 */
@Getter
@Setter
@ParameterObject
public class Query {
    @Parameter(description = "模糊搜索")
    private String q;
    @Parameter(description = "过滤条件")
    private String filter;
}
