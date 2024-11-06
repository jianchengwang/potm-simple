package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.CdcLogQuery;
import org.example.potm.svc.sys.model.vo.CdcLogInfoVO;
import org.example.potm.svc.sys.model.vo.CdcLogRowDetailVO;
import org.example.potm.svc.sys.service.CdcLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/9
 */
@RestController
@RequestMapping("/api/admin/sys/cdcLog")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-操作日志")
public class CdcLogController {

    private final CdcLogService cdcLogService;

    @SaCheckPermission("admin:sys:cdcLog:page")
    @Operation(summary = "操作日志分页", description = "操作日志分页")
    @GetMapping("page")
    public Response<IPage<CdcLogInfoVO>> page(PageInfo pageInfo, CdcLogQuery param) {
        return Response.ok(cdcLogService.page(pageInfo, param));
    }

    @SaCheckPermission("admin:sys:cdcLog:page")
    @Operation(summary = "详细记录", description = "详细记录")
    @GetMapping("/{logInfoId}")
    public Response<List<CdcLogRowDetailVO>> getCdcLogRowDetails(@PathVariable Long logInfoId) {
        return Response.ok(cdcLogService.getCdcLogRowDetails(logInfoId));
    }

    @SaCheckPermission("admin:sys:cdcLog:page")
    @Operation(summary = "批量删除", description = "批量删除")
    @DeleteMapping("{ids}")
    public Response<Void> delete(@PathVariable String ids) {
        cdcLogService.deleteByIds(ids);
        return Response.ok();
    }
}
