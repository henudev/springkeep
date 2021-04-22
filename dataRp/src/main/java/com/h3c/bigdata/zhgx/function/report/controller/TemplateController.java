package com.h3c.bigdata.zhgx.function.report.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.report.annotation.CurrentUser;
import com.h3c.bigdata.zhgx.function.report.base.PublicResultConstant;
import com.h3c.bigdata.zhgx.function.report.config.ResponseHelper;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateAddBean;
import com.h3c.bigdata.zhgx.function.report.service.TemplateManageService;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName test
 * @Description 模板处理
 * @Author zzzzitai
 * @Date 2018/11/14 12:10
 * @Version 1.0
 **/

@RestController
@RequestMapping(value = "/template")
@Api(description = "模板处理控制器")
@LoginOpen
public class TemplateController  extends BaseController {

    @Autowired
    TemplateManageService templateManageService;
    @Autowired
    AuthDptService departmentService;

    /**
     * @return
     * @Author
     * @Description 添加模板
     * @Date
     * @Param template 模板实体
     **/
    @ApiOperation(value = "增加模板", notes = "增加模板")
    @PostMapping(value = "/add", produces = {"application/json"})
    @Log(module = ModuleType.TEMPLATE,action = BusinessType.INSERT)
    public ResponseModel templateAdd(@RequestBody TemplateAddBean template) throws Exception {
        String userId = getUserIdByToken();
        templateManageService.addTemplate(template,userId);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }

    /**
     * @return
     * @Author
     * @Description 删除模板
     * @Date
     * @Param templateIdList 模板id列表
     **/
    @ApiOperation(value = "删除模板", notes = "删除模板")
    @Log(module = ModuleType.TEMPLATE, action = BusinessType.BATCH_DELETE)
    @PostMapping(value = "/delete")
    public ResponseModel templateDelete(@RequestBody List<Integer> templateIdList) throws Exception {
        templateManageService.deleteTemplate(templateIdList);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }


    /**
     * @return
     * @Author
     * @Description 导出模板
     * @Date
     * @Param templateId 模板ID
     **/
    @PermitAll
    @ApiOperation(value = "模板导出", notes = "模板导出")
    @GetMapping(value = "/output")
    public void getTemplateList(@ApiParam(name = "templateId", value = "模板id")
                                @RequestParam(value = "templateId", required = true) Integer templateId,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        templateManageService.exportToExcel(request,templateId, response);
    }

    /**
     * @param pageNum  起始页
     * @param pageSize 页大小
     * @param keyword  关键字
     * @return
     * @Author
     * @Description 获取模板列表
     * @Date
     **/
    @ApiOperation(value = "模板列表", notes = "模板列表")
    @GetMapping(value = "/pageList")

    public ResponseModel<PageResult> getTemplateList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                     @RequestParam(value = "keyword") String keyword,
                                                     @RequestParam(value = "departmentId") String departmentId,
                                                     @RequestParam(value = "searchDepId", required = false) String searchDepId,
                                                     @RequestParam(value = "isUsed", required = false) String isUsed)
            throws Exception {
        String userId = getUserIdByToken();
        return ResponseHelper.buildResponseModel(templateManageService.getTemplateList(new Page<>(pageNum, pageSize),
                keyword, departmentId, userId, searchDepId,isUsed));
    }

    /**
     * 获取所有模板
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "模板所有", notes = "模板所有")
    @GetMapping(value = "/list")

    public ResponseModel getTemplateAll(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "tag", required = false) String tag,
                                        @CurrentUser AuthUserInfoEntity user) throws Exception {
        AuthDepartmentInfoEntity departmentInfoEntity = departmentService.selectDeptByUserId(user.getUserId());
        String departmentCode = departmentInfoEntity.getDepartmentCode();
        if (departmentCode.length() > 5) {
            departmentCode = departmentCode.substring(0, 5);
        }
        return ResponseHelper.buildResponseModel(templateManageService.getTemplateList(tag, name, departmentCode));
    }

    /**
     * 查询模板。
     *
     * @param tempNumber
     * @return
     */
    @ApiOperation(value = "根据模板编号查询模板及列", notes = "根据模板编号查询模板及列")
    @GetMapping(value = "/queryByNumber/{tempNumber}")

    public ResponseModel queryTempByNumber(@ApiParam(name = "tempNumber", value = "模板编号")
                                           @PathVariable("tempNumber") String tempNumber,
                                           @RequestParam(value = "templateSourceNameEn", required = true)
                                                   String templateSourceNameEn) {
        return ResponseHelper.buildResponseModel(templateManageService.queryTempByNumber(tempNumber,templateSourceNameEn));
    }

    /**
     * 修改模板.
     *
     * @param template
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改模板", notes = "修改模板")
    //@Log(module = ModuleType.TEMPLATE, action = BusinessType.UPDATE)
    @PostMapping(value = "/update")

    public ResponseModel updateTempAndDescription(@RequestBody TemplateAddBean template) throws Exception {
        String userId = getUserIdByToken();
        templateManageService.updateTempAndDescription(template,userId);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }

    /**
     * 查询模板所有属性。
     *
     * @param templateId
     * @return
     */
    @ApiOperation(value = "根据模板编号查询属性列", notes = "根据模板编号查询属性列")
    @GetMapping(value = "/queryItems/{tempNumber}")

    public ResponseModel queryItemsByNumber(@ApiParam(name = "templateId", value = "模板ID")
                                            @PathVariable("templateId") Integer templateId) {
        return ResponseHelper.buildResponseModel(templateManageService.queryItemsByNumber(templateId));
    }

    /**
     * 检查表字段是否存在
     *
     * @param
     * @return
     */
    @ApiOperation(value = "检查表字段是否存在", notes = "检查表字段是否存在")
    @GetMapping(value = "/checkItemExist")

    public ResponseModel checkItemExist(
            @RequestParam(value = "templateSourceName") String templateSourceName,
            @RequestParam(value = "tableName", required = true) String tableName,
            @RequestParam(value = "item", required = true) String item) {
        return ResponseHelper.buildResponseModel(templateManageService.checkItemExist(templateSourceName,
                tableName, item));
    }

    /**
     * 检查模板名/表名是否存在
     *
     * @param
     * @return
     */
    @ApiOperation(value = "检查模板名/表名内是否存在待写入的数值", notes = "检查模板名/表名内是否存在待写的入数值")
    @GetMapping(value = "/checkTemOrTabExist")

    public ResponseModel checkTemOrTabExist(@RequestParam(value = "temOrTabName", required = true) String temOrTabName,
                                            @RequestParam(value = "item", required = true) String item) {
        return ResponseHelper.buildResponseModel(templateManageService.checkTemOrTabExist(temOrTabName, item));
    }

    /**
     * @Description: 批量上传模板字段
     * @Param:
     * @Author: l17503
     * @Date: 2019/9/2 14:23
     */
    //@ApiOperation(value = "批量上传模板字段", notes = "批量上传模板字段")
    @PostMapping(value = "/batchImportField")

    public ResponseModel batchImportField(@RequestParam("file") MultipartFile file) throws Exception{

        return ResponseHelper.buildResponseModel(templateManageService.batchImportField(file));

    }


    @ApiOperation(value = "批量停用表格状态", notes = "批量停用表格状态")
    @Log(module = ModuleType.TEMPLATE, action = BusinessType.UPDATE_STATUS)
    @RequestMapping(value = "/updateTemplateUsed", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ResponseModel updateUsersStatus(@RequestBody TemplateAddBean templateAddBean) {

        return ResponseHelper.buildResponseModel( templateManageService.updateTemplateStatus(templateAddBean));

    }

    /**
     * @return
     * @Author
     * @Description 检查模板中是否存在数据
     * @Date
     * @Param templateIdList 模板id列表
     **/
//
    @PostMapping(value = "/checkDataExist")
    @ApiOperation(value = "检查模板中是否存在数据", notes = "检查模板中是否存在数据")

    public ResponseModel checkDataExist(@RequestBody List<Integer> templateIdList) throws Exception {
        return ResponseHelper.buildResponseModel(templateManageService.checkTemplateDataExist(templateIdList));
    }


    /**
     * 导出数据
     * @param updateTime 上传时间
     */
    @PermitAll
    @GetMapping(value = "/exportData")
    public void getTableList(@RequestParam(value = "templateId") Integer templateId,
                             @RequestParam(value = "updateTime", required = false) String updateTime,
                             @RequestParam(value = "dir") String dir,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("导出开始时间："+format.format(new Date()));
        templateManageService.exportData(templateId,updateTime,dir,request, response);
        System.out.println("导出结束时间："+format.format(new Date()));
    }

    @PermitAll
    @GetMapping(value = "/getTemplateType")
    public ResponseModel getTemplateType(@RequestParam(value = "templateId") Integer templateId) throws Exception {
        return ResponseHelper.buildResponseModel(templateManageService.countAnnex(templateId));
    }

    /**
     * 模板插入
     * @throws Exception
     */
    @PermitAll
    @GetMapping(value = "/move1")
    @ResponseBody
    public void getTemplateType() throws Exception {
        templateManageService.move();
    }

    /**
     * 数据插入
     * @throws Exception
     */
    @PermitAll
    @GetMapping(value = "/move2")
    @ResponseBody
    public void getTemplateType2() throws Exception {
        templateManageService.move2();
    }

    /**
     * 数据量更新
     */
    @PermitAll
    @GetMapping(value = "/move3")
    @ResponseBody
    public void getTemplateType3() throws Exception {
        templateManageService.move3();
    }
}
