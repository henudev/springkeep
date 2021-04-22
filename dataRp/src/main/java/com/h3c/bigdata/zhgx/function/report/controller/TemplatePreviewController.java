package com.h3c.bigdata.zhgx.function.report.controller;

import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.report.config.ResponseHelper;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;

import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import com.h3c.bigdata.zhgx.function.report.service.TemplatePreviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 首页、模板总览、模板预览方法类入口
 * @Author: w15112
 * @CreateDate: 2019/06/03 11:11
 * @UpdateUser: w15112
 * @UpdateDate: 2019/06/03 11:11
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/preview")
@Api(description = "模板总览/预览")
@LoginOpen
public class TemplatePreviewController extends BaseController {

    @Autowired
    private TemplatePreviewService templatePreviewService;

    /**
     * @return
     * @Author
     * @Description 获取模板预览统计值（模板总量、数据总量、模板总完成率 ）
     * @Date
     **/
    @ApiOperation(value = "模板预览统计信息", notes = "模板预览统计信息")
    @GetMapping(value = "/getPreviewInfo")
    public ResponseModel<Map> getTemplateList( @RequestParam(value = "departmentId") String  departmentId)
             {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(templatePreviewService.getPreviewInfo(userId,departmentId));
    }

    /**
     * @return
     * @Author
     * @Description 获取部门名称、模板数量、完成率的列表
     * @Date
     **/
    @ApiOperation(value = "模板预览-部门列表", notes = "模板预览-部门列表")
    @GetMapping(value = "/getDepartmentList")
    public ResponseModel<PageResult> getDepartmentList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "field", defaultValue = "dataSum",required = false) String field,
            @RequestParam(value = "dir", defaultValue = "desc",required = false) String dir,
            @RequestParam(value = "searchDepId", required = false) String searchDepId,
            @RequestParam(value = "departmentId") String departmentId)
            {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(templatePreviewService.
                getDepartmentList(pageNum,pageSize,userId,departmentId,field,dir,searchDepId));
    }

    /**
     * @return
     * @Author
     * @Description 根据部门id获取模板列表信息
     * @Date
     **/
    @ApiOperation(value = "模板预览-模板列表", notes = "模板预览-模板列表")
    @PostMapping(value = "/getTemplatesByDepId")
    public ResponseModel<PageResult> getTemplatesByDepId(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "field", defaultValue = "co.update_time") String field,
            @RequestParam(value = "dir", defaultValue = "desc") String dir,
            @RequestParam(value = "searchDepId", required = false) String searchDepId,
            @RequestBody TemplateModel templateModel)
            {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(templatePreviewService.getTemplatesByDepId(pageNum,pageSize,field,dir,
                templateModel,userId,searchDepId));
    }

    /**
     * @return
     * @Author
     * @Description 数据填报页面统计信息：模板总量、未填报模板数量、已填报模板数量
     * @Date
     **/
    @ApiOperation(value = "模板统计信息", notes = "模板统计信息")
    @GetMapping(value = "/getTemplateCountInfo")
    public ResponseModel<Map> getTemplateCountInfo(
            @RequestParam(value = "departmentId", required = true) String departmentId) {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(templatePreviewService.getTemplateCountInfo(userId,departmentId));
    }

   /**
   * @Description: 根据数据库表名查询表字段信息
   * @param  templateId
   * @return
   * @Author: w15112
   * @Date: 2019/6/6 11:14
   */

    @ApiOperation(value = "根据数据库表名查询表字段信息", notes = "根据数据库表名查询表字段信息")
    @GetMapping(value = "/selectTableDescriptionById")
    public ResponseModel<List<TableDescriptionEntity>> selectTableDescriptionById(
            @RequestParam(value = "templateId", required = true) Integer templateId) {
        return ResponseHelper.buildResponseModel(templatePreviewService.selectTableDescriptionById(templateId));
    }

    /**
     * @return
     * @Author
     * @Description 根据模板id查询填报记录列表
     * @Date
     **/

    @ApiOperation(value = "根据表名查询填报记录列表", notes = "根据表名查询填报记录列表")
    @GetMapping(value = "/getTableFillLogsByTemplateId")
    public ResponseModel<PageResult> getTableFillLogsByTableName(
            @RequestParam(value = "templateId", required = true) Integer templateId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseHelper.buildResponseModel(templatePreviewService.
                getTableFillLogsByTableName(templateId, pageNum, pageSize));
    }
}
