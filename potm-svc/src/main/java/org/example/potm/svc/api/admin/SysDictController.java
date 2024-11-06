package org.example.potm.svc.api.admin;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.potm.svc.sys.model.dto.SysDictItemDTO;
import org.example.potm.svc.sys.model.query.SysDictQuery;
import org.example.potm.svc.sys.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.dict.SysDict;
import org.example.potm.framework.config.dict.SysDictItem;
import org.example.potm.framework.pojo.PageInfo;
import org.example.potm.framework.pojo.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@RestController
@RequestMapping("/api/admin/sys/dict")
@RequiredArgsConstructor
@Tag(name = "管理端-系统模块-字典管理")
public class SysDictController {

    private final SysDictService dictService;

    @Operation(summary = "字典分页", description = "字典分页")
    @GetMapping("page")
    public Response<IPage<SysDict>> page(PageInfo pageInfo, SysDictQuery param) {
        return Response.ok(dictService.page(pageInfo, param));
    }

    @SaIgnore
    @Operation(summary = "查询所有", description = "查询所有")
    @GetMapping("fetchAll")
    public Response<List<SysDict>> fetchAll(boolean forceLoadFromDb) {
        return Response.ok(dictService.fetchAll(forceLoadFromDb));
    }

    @SaIgnore
    @Operation(summary = "查询字典项列表", description = "查询字典项列表")
    @GetMapping("{dictKey}/itemList")
    public Response<List<SysDictItem>> fetchItemList(@PathVariable String dictKey, boolean forceLoadFromDb) {
        return Response.ok(dictService.fetchItemList(dictKey, forceLoadFromDb));
    }

    @Operation(summary = "修改字典项", description = "修改字典项")
    @PostMapping("{dictKey}/itemList")
    public Response<Void> updateItemList(@PathVariable String dictKey, @RequestBody List<SysDictItemDTO> itemList) {
        dictService.updateItemList(dictKey, itemList);
        return Response.ok();
    }

}
