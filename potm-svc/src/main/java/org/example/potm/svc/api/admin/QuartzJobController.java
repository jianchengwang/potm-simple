package org.example.potm.svc.api.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.quartz.model.dto.QuartzJobSaveDTO;
import org.example.potm.svc.quartz.model.query.QuartzJobQuery;
import org.example.potm.svc.quartz.model.vo.QuartzJobVO;
import org.example.potm.svc.quartz.service.QuartzJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/quartz/job")
@Tag(name = "管理端-定时任务")
@RequiredArgsConstructor
public class QuartzJobController {

    private final QuartzJobService quartzJobService ;

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "任务查询")
    @GetMapping("page")
    public Response<IPage<QuartzJobVO>> page(PageInfo pageInfo, QuartzJobQuery param){
        return Response.ok(quartzJobService.page(pageInfo, param));
    }

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "任务查询")
    @GetMapping("{id}")
    public Response<QuartzJobVO> getById(@PathVariable Long id){
        return Response.ok(quartzJobService.getById(id));
    }

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "任务新增或者更新")
    @PostMapping("")
    public Response<Void> saveOrUpdate(@RequestBody QuartzJobSaveDTO param){
        quartzJobService.saveOrUpdate(param);
        return Response.ok();
    }

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "停止任务")
    @PostMapping("{id}/pause")
    public Response<Void> pause(@PathVariable("id") Long id) {
        quartzJobService.pause(id);
        return Response.ok();
    }

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "恢复任务")
    @PostMapping("{id}/resume")
    public Response<Void> resume(@PathVariable("id") Long id) {
        quartzJobService.resume(id);
        return Response.ok();
    }

    @SaCheckPermission("admin:sys:quartzJob")
    @Operation(summary = "执行一次")
    @PostMapping("{id}/runOnce")
    public Response<Void> runOnce(@PathVariable("id") Long id) {
        quartzJobService.runOnce(id);
        return Response.ok();
    }
}
