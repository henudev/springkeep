package com.h3c.bigdata.zhgx.function.report.controller;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.report.config.ResponseHelper;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.entity.DataAnnexEntity;
import com.h3c.bigdata.zhgx.function.report.serviceImpl.FileProcessServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件操作.
 */
@Controller
@Api(description = "文件操作")
@RequestMapping("/file")
public class FileProcessController {

    @Autowired
    FileProcessServiceImpl fileProcessService;
    /**
     * 上传多个文件.
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @PermitAll
    @ResponseBody
    public ResponseModel uploadFile(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {
        return fileProcessService.uploadFile(file, request);
    }

    /**
     * 根据数据记录查询附件列表
     * @param annexIds
     * @throws IOException
     */
    @GetMapping("/annexlist")
    @PermitAll
    @ResponseBody
    public ResponseModel getAnnexList(@RequestParam("annexIds") String annexIds,
                                      HttpServletRequest request) {
        return ResponseHelper.buildResponseModel(fileProcessService.getAnnexList(annexIds, request));
    }

    /**
     * 删除附件
     * @param annexIds
     * @throws IOException
     */
    @GetMapping("/delete")
    @PermitAll
    @ResponseBody
    public ResponseModel deleteAnnex(@RequestParam("annexIds") String annexIds) {
        return ResponseHelper.buildResponseModel(fileProcessService.deleteAnnex(annexIds));
    }

    /**
     * 删除附件组
     * @param groupId
     * @throws IOException
     */
    @Deprecated
    @GetMapping("/deltegroup")
    @PermitAll
    @ResponseBody
    public ResponseModel deleteAnnexGroup(@RequestParam("groupId") String groupId,
                                     HttpServletRequest request) {
        return ResponseHelper.buildResponseModel(fileProcessService.deleteAnnexByGroupId(groupId, request));
    }

    /**
     * 根据数据记录下载附件列表(如果是单个直接下载，多个压缩下载)
     * @param annexIds
     * @throws IOException
     */
    @GetMapping("/download")
    @PermitAll
    public void downAnexList(@RequestParam("annexIds") String annexIds,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        fileProcessService.downLoadAnnexByGroupId(annexIds, request, response);

    }

    /**
     * 删除附件时，更新ES和MySQL
     * @throws IOException
     */
    @GetMapping("/delUpdate")
    @PermitAll
    @ResponseBody
    public ResponseModel delUpdate(@RequestParam("number") String number,
                              @RequestParam("dataId") String dataId,
                              @RequestParam("annexId") String annexId,
                              @RequestParam("tableName") String tableName,
                              @RequestParam("key") String key) {

        try {
            fileProcessService.delUpdate(number, dataId, annexId, tableName, key);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseHelper.internalServerError("uodata failed");
        }
        return ResponseHelper.buildResponseModel("uodata success");
    }

    /**
     * 根据模板名称获取附件列表
     * @param templateName
     * @return
     */
    @GetMapping("/fileList")
    @PermitAll
    @ResponseBody
    public ApiResult getFileListByTempalteName(@RequestParam("templateName") String templateName,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResult list = fileProcessService.getAnnexListByTempalteName(templateName, page, pageSize);
        return list;
    }
}
