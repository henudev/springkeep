package com.h3c.bigdata.zhgx.function.report.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateSourceEntity;
import com.h3c.bigdata.zhgx.function.report.service.TemplateSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 模板数据源管理
 * @date 2019-10-12 15:22:47
 */
@RestController
@RequestMapping(value = "/templateSource")
@Api(description = "模板数据源管理")
@LoginOpen
public class TemplateSourceController extends BaseController {

    @Autowired
    private TemplateSourceService templateSourceService;

    @LoginOpen
    @ApiOperation(value = "模板数据源列表", notes = "模板数据源列表")
    @PostMapping(value = "/queryTemplateSourceList")
    public ApiResult queryTemplateSource( @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "dir") String dir,
                                          @RequestParam(value = "orderBy") String orderBy,
                                          @RequestBody TemplateSourceEntity templateSourceEntity){

        return templateSourceService.queryTemplateSourceList(pageNum,pageSize,orderBy,dir,templateSourceEntity);
    }

    @LoginOpen
    @ApiOperation(value = "更新模板数据源", notes = "更新模板数据源")
    @Log(module = ModuleType.DATA_SOURCE, action = BusinessType.UPDATE)
    @PostMapping(value = "/updateTemplateSource")
    public ApiResult updateTemplateSource( @RequestBody TemplateSourceEntity templateSourceEntity){
        String userId = getUserIdByToken();
        templateSourceEntity.setUpdateUser(userId);
        return templateSourceService.updateTemplateSource(templateSourceEntity);
    }

    @LoginOpen
    @ApiOperation(value = "删除模板数据源", notes = "删除模板数据源")
    @Log(module = ModuleType.DATA_SOURCE, action = BusinessType.DELETE)
    @DeleteMapping(value = "/deleteSourceById")
    public ApiResult deleteSourceById(@RequestParam(value = "sourceId") String sourceId){
        return templateSourceService.deleteTemplateSouece(sourceId);
    }

    @LoginOpen
    @ApiOperation(value = "查询数据源的英文名或汉语名是否已经存在", notes = "查询数据源的英文名或汉语名是否已经存在")
    @PostMapping(value = "/countSource")
    public ApiResult countSource( @RequestBody TemplateSourceEntity templateSourceEntity){
        return templateSourceService.countTemplateSource(templateSourceEntity);
    }

    @LoginOpen
    @ApiOperation(value = "插入模板数据源", notes = "插入模板数据源")
    @Log(module = ModuleType.DATA_SOURCE, action = BusinessType.INSERT)
    @PostMapping(value = "/insertTemplateSource")
    public ApiResult insertTemplateSource( @RequestBody TemplateSourceEntity templateSourceEntity){
        String userId = getUserIdByToken();
        templateSourceEntity.setUpdateUser(userId);
        return templateSourceService.insertTemplateSource(templateSourceEntity);
    }


}
