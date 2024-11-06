package org.example.potm.svc.api.common;

import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.example.potm.svc.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.example.potm.framework.config.cdc.NotLog;
import org.example.potm.framework.config.web.AbstractController;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.pojo.Response;
import org.example.potm.plugins.storage.pojo.SObject;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Tag(name = "公共模块-对象存储")
@RestController
@RequestMapping("api/common/storage")
@RequiredArgsConstructor
public class StorageController extends AbstractController {

    private final StorageService storageService;

    /**
     * 上传文件接口，需要校验权限
     * @param module 值因为不同module的上传逻辑可能不一样，所以这里根据不同module调用不同接口
     * @param prefix 资源前缀，相当于目录概念，或是关联编号
     * @param file 上传文件
     * @return 上传文件信息
     */
    @NotLog
    @Operation(summary = "上传", description = "文件上传接口")
    @PostMapping(value = "upload/{module}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<SObject> upload(
            @Parameter(description = "文件上传模型", required = true) @PathVariable("module") FileModuleEnum module,
            @Parameter(description = "资源前缀，相当于目录概念，或是关联编号") @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix,
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "强制覆盖") @RequestParam(value = "forcedPut", required = false) boolean forcedPut
    ) {

        return Response.ok(uploadFile(module, prefix, file, forcedPut, true));
    }

    /**
     * 批量上传文件接口，需要校验权限
     * @param module module值因为不同module的上传逻辑可能不一样，所以这里根据不同module调用不同接口
     * @param prefix 资源前缀，相当于目录概念，或是关联编号
     * @param files 上传文件
     * @return 上传文件信息
     */
    @NotLog
    @Operation(summary = "批量上传", description = "文件批量上传接口")
    @PostMapping(value = "uploadBatch/{module}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<List<SObject>> uploadBatch(
            @Parameter(description = "文件上传模块", required = true) @PathVariable("module") FileModuleEnum module,
            @Parameter(description = "资源前缀，相当于目录") @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix,
            @Parameter(description = "批量上传的文件列表") @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "强制覆盖") @RequestParam(value = "forcedPut", required = false) boolean forcedPut
    ) {
        List<SObject> objectList = batchUploadFile(module, prefix, files, forcedPut, true);
        return Response.ok(objectList);
    }

    /**
     * 下载文件接口，需要校验权限
     * @param request
     * @param response
     */
    @NotLog
    @Operation(summary = "下载", description = "文件下载接口")
    @GetMapping("download/**")
    public void download(final HttpServletRequest request, final HttpServletResponse response) {
        downloadFile(request, response);
    }

    @NotLog
    @Operation(summary = "根据dbId下载文件", description = "根据dbId下载文件接口")
    @GetMapping("downloadByDbId/{dbId}")
    public void downloadByDbId(final HttpServletResponse response, @PathVariable String dbId) {
        downloadFile(dbId, response);
    }

    @NotLog
    @Operation(summary = "批量下载关联文件", description = "批量下载关联文件")
    @GetMapping("downloadByRefId/{module}/{refId}")
    public void downloadByRefId(final HttpServletResponse response, @PathVariable("module") FileModuleEnum module, @PathVariable String refId, String fileName) {
        try {
            List<SObject> sObjectList = storageService.downloadByRefId(refId, module);
            if(CollectionUtils.isEmpty(sObjectList)) {
                throw new ClientException("附件列表为空", null);
            }
            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                response.setHeader("filename", fileName);
                response.setHeader("Access-Control-Expose-Headers", "filename");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                for (SObject sObject : sObjectList) {
                    if (sObject != null) {
                        InputStream is = sObject.getInputStream();
                        String fileUrl = sObject.getOriginalFileName();
                        zos.putNextEntry(new ZipEntry(fileUrl));
                        IOUtils.copy(is, zos);
                    }
                }
            }
        } catch (Exception e) {
            throw new ClientException(null, e);
        }
    }

    protected SObject uploadFile(FileModuleEnum module, String prefix, MultipartFile file, boolean forcedPut, boolean insertDb) {
        return batchUploadFile(module, prefix,  new MultipartFile[]{file}, forcedPut, insertDb).get(0);
    }

    protected List<SObject> batchUploadFile(FileModuleEnum module, String prefix, MultipartFile[] files, boolean forcedPut, boolean insertDb) {
//        if(module != FileModuleEnum.PUBLIC) {
//            check permission
//        }
        return storageService.upload(module, prefix, forcedPut, insertDb, files);
    }

    private void downloadFile(final HttpServletRequest request, final HttpServletResponse response) {
        String fullPath = getBestMatchPath(request);
        // check permission
        SObject object = storageService.download(fullPath);
        write(response, object.getFileName(), object.getInputStream(), object.getMimeType());
    }

    private void downloadFile(final String dbId, final HttpServletResponse response) {
        SObject object = storageService.downloadByDbId(dbId);
        String fullPath = object.getKey();
        write(response, object.getFileName(), object.getInputStream(), object.getMimeType());
    }

    private String getBestMatchPath(final HttpServletRequest request) {
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        // 获取通配符后面的资源文件URI
        AntPathMatcher apm = new AntPathMatcher();
        return apm.extractPathWithinPattern(bestMatchPattern, path);
    }
}