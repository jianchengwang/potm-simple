package org.example.potm.svc.api.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.query.SysNotifyQuery;
import org.example.potm.svc.sys.model.vo.SysNotifyVO;
import org.example.potm.svc.sys.service.SysNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.permission.user.TokenUserContextHolder;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 管理端-系统模块-系统通知管理
 * </p>
 *
 * @author jianchengwang
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/api/admin/sys/notify")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-系统通知管理")
public class SysNotifyController {

    private final SysNotifyService service;

    @Operation(summary = "分页", description = "分页")
    @GetMapping("page")
    public Response<IPage<SysNotifyVO>> page(PageInfo pageInfo, SysNotifyQuery param) {
        Long userId = TokenUserContextHolder.currentUserId();
        param.setUserId(userId);
        return Response.ok(service.page(pageInfo, param));
    }

    @Operation(summary = "批量已读", description = "批量已读")
    @PostMapping("batchRead")
    public Response<Void> batchRead(@RequestParam(value = "idList", required = false) List<String> idList) {
        Long userId = TokenUserContextHolder.currentUserId();
        service.batchRead(userId, idList);
        return Response.ok();
    }

    @Operation(summary = "删除", description = "删除")
    @DeleteMapping("batchDelete")
    public Response<Void> batchDelete(@RequestParam(value = "idList", required = false) List<String> idList) {
        Long userId = TokenUserContextHolder.currentUserId();
        service.batchDelete(userId, idList);
        return Response.ok();
    }
}