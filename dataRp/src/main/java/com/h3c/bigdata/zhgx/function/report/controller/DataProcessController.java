package com.h3c.bigdata.zhgx.function.report.controller;


import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.report.config.ResponseHelper;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.model.*;
import com.h3c.bigdata.zhgx.function.report.service.DataProcessService;
import com.h3c.bigdata.zhgx.function.report.service.EsDataOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName DataCollectorController
 * @Description 数据上传
 * @Author zzzzitai
 * @Date 2018/11/14 12:11
 * @Version 1.0
 **/
@Api(description = "模板数据处理")
@RestController
@RequestMapping(value = "/data")
@LoginOpen
public class DataProcessController extends BaseController {

    @Autowired
    private DataProcessService dataProcessService;
    @Autowired
    EsDataOperationService esDataOperationService;

    @PostMapping(value = "/upload")
    @Log(module = ModuleType.DATA_FILL, action = BusinessType.UPLOAD)
    public ResponseModel uploadFile(@ApiParam("上传文件") @RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "templateId") Integer templateId) throws Exception {
        return ResponseHelper.buildResponseModel(dataProcessService.uploadFile(file, templateId));
    }


    /**
     * @return
     * @Author
     * @Description 从excel上传数据，并校验.
     * @Date
     * @Param fileName
     * 上传文件名
     **/
    @ApiOperation(value = "数据提交", notes = "提交数据，并校验")
    @PostMapping(value = "/save")
    @Log(module = ModuleType.DATA_REPORT, action = BusinessType.FILL)
    public ResponseModel checkCommitData(HttpServletRequest request,
                                         @RequestBody ReportData reportData)
            throws Exception {
        String userId = getUserIdByToken();
        Map<String, Object> data = dataProcessService.saveData(reportData, userId);
        return ResponseHelper.buildResponseModel(data);
    }

    /**
     　　* @Description: 数据重复时执行的策略
     　　* @param
     　　* @return
     　　* @throws
     　　* @date 2019/1/15 12:51
     　　*/
    @ApiOperation(value = "数据重复时执行的策略", notes = "数据重复时执行的策略")
    @PostMapping(value = "/strategy")
    @PermitAll
    public ResponseModel strategyData(@RequestBody Map<String,String> map)
            throws Exception {
        return ResponseHelper.buildResponseModel(dataProcessService.strategyData(map));
    }

    /**
     * 　　* @Description: 删除数据
     * 　　* @param List<String> idList
     * 　　* @return
     * 　　* @throws
     * 　　* @date 2019/1/7 15:46
     *
     */
    @ApiOperation(value = "数据删除", notes = "用户上传数据删除")
    @PostMapping(value = "/delete")
    @Log(module = ModuleType.DATA_FILL, action = BusinessType.BATCH_DELETE)

    public ResponseModel deleteData(@ApiParam(value = "待删除数据") @RequestBody DelData delData) throws Exception {
        String userId = getUserIdByToken();
        dataProcessService.deleteData(delData, userId);
        return ResponseHelper.buildResponseModel("删除成功");
    }

    @ApiOperation(value = "一键清空模板数据", notes = "一键清空模板数据")
    @GetMapping(value = "/clear")
    @Log(module = ModuleType.DATA_FILL, action = BusinessType.BATCH_DELETE)
    @PermitAll
    public ResponseModel deleteAllData(@RequestParam(value = "templateId") Integer templateId) throws Exception {
        dataProcessService.deleteAllDataByTemplateId(templateId);
        return ResponseHelper.buildResponseModel("删除成功");
    }


    /**
     * @param
     * @return
     * @Description: 删除填报记录
     * @Author: w15112
     * @Date: 2019/8/20 14:26
     */
    @ApiOperation(value = "删除填报记录", notes = "删除填报记录")
    @PostMapping(value = "/deleteFillLogByUpdateTime")
    @Log(module = ModuleType.DATA_FILL, action = BusinessType.DELETE_FILLLOG)

    public ResponseModel deleteFillLogByUpdateTime(@RequestParam(value = "templateId") Integer templateId,
                                                   @RequestParam(value = "updateTime") String updateTime)
            throws Exception {
        String userId = getUserIdByToken();
      dataProcessService.deleteFillLogByUpdateTime(templateId, updateTime,userId);
        return ResponseHelper.buildResponseModel("删除成功");
    }
    /**
     * 用户上传数据更新.
     *
     * @param data
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "数据更新", notes = "用户上传数据更新")
    @PostMapping(value = "/update")
    @Log(module = ModuleType.DATA_FILL, action = BusinessType.UPDATE)

    public ResponseModel updateData(@RequestBody ReportData data) throws Exception {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(dataProcessService.updateData(data, userId));
    }

    /**
     * 数据查询.
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "数据查询", notes = "数据查询")
    @PostMapping(value = "/query")
    @PermitAll
    public ResponseModel queryData(@RequestBody QueryData queryData) throws Exception {
        return ResponseHelper.buildResponseModel(dataProcessService.queryData(queryData));
    }


    /**
     * @param
     * @return
     * @Description: 关键字全局检索
     * @Author: w15112
     * @Date: 2019/8/17 17:43
     */
    @ApiOperation(value = "数据检索", notes = "用户上传数据检索")
    @PostMapping(value = "/keySearch")
    @PermitAll
    public ResponseModel searchCommitDataByKeyword(@RequestBody EsQuery esQuery
    ) throws Exception {

        return ResponseHelper.buildResponseModel(esDataOperationService.searchByKeywords(esQuery));
    }

    @ApiOperation(value = "将现场表数据添加至es中", notes = "将现场表数据添加至es中")
    @GetMapping(value = "/InsertOldDataToEs")
    @PermitAll
    public ResponseModel InsertOldDataToEs(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) throws Exception {
           dataProcessService.InsertOldDataToEs(pageNum);
        return   ResponseHelper.buildResponseModel("添加第"+pageNum+"页es数据成功");
    }

    @ApiOperation(value = "批量更新整数、小数字段数据", notes = "批量更新整数、小数字段数据")
    @GetMapping(value = "/editOldDataToEs")
    @PermitAll
    public ResponseModel editOldDataToEs() throws Exception {
        dataProcessService.editOldDataToEs();
        return   ResponseHelper.buildResponseModel("批量更新整数、小数字段数据成功");
    }
    @ApiOperation(value = "将现场单条数据添加至es中", notes = "将现场表数据添加至es中")
    @GetMapping(value = "/InsertSingleDataToEs")
    @PermitAll
    public ResponseModel InsertSingleDataToEs(@RequestParam(value = "templateId") Integer templateId)
            throws Exception {
        dataProcessService.InsertSingleDataToEs(templateId);
        return   ResponseHelper.buildResponseModel("添加es数据成功");
    }
}
