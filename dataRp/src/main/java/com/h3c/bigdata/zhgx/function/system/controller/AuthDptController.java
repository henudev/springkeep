package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: h17338
 * @Description: 部门维护控制器
 * @Date: 2018/7/30
 */
@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthDptController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthDptController.class);

    @Autowired
    private AuthDptService authDptService;


    @Log(module = ModuleType.SYSTEM_DEPART, action = BusinessType.INSERT)
    @ApiOperation(value = "新增部门信息", notes = "新增部门信息")
    @RequestMapping(value = "/dptInfo_insert", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> insertDptInfoController(@RequestBody AuthDepartmentInfoEntity authDepartmentInfoEntity) {
        ApiResult result;
        try {
            result = authDptService.addDptInfo(authDepartmentInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("新增部门信息失败!", e);
            return ApiResult.fail("新增部门信息失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_DEPART, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更部门信息", notes = "变更部门信息")
    @RequestMapping(value = "/dptInfo_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateDptInfoController(@RequestBody AuthDepartmentInfoEntity authDepartmentInfoEntity) {
        ApiResult result;
        try {
            result = authDptService.updateDptInfo(authDepartmentInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("变更部门信息失败!", e);
            return ApiResult.fail("变更部门信息失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_DEPART, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更部门地图信息", notes = "变更部门地图信息")
    @RequestMapping(value = "/dptLocInfo_update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<?> updateDptLocInfoController(@RequestBody AuthDepartmentInfoEntity authDepartmentInfoEntity) {
        ApiResult result;
        try {
            result = authDptService.updateLocDptInfo(authDepartmentInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("变更部门地理信息失败!", e);
            return ApiResult.fail("变更部门地理信息失败！");
        }
    }


    @ApiOperation(value = "查询部门信息", notes = "查询部门信息")
    @RequestMapping(value = "/dptInfo_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryDptInfoController(@RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                               @RequestParam(value = "field", defaultValue = "id") String field,
                                               @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                               @RequestBody AuthDepartmentInfoEntity authDepartmentInfoEntity) {
        ApiResult result;
        try {
            result = authDptService.queryDptInfo(page, pageSize, field, dir, authDepartmentInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("查询部门信息失败!", e);
            return ApiResult.fail("查询部门信息失败！");
        }
    }

    @LoginOpen
    @ApiOperation(value = "查询部门信息", notes = "查询部门信息")
    @RequestMapping(value = "/dptInfo_queryPinpoint", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryDptInfoPinpointController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "field", defaultValue = "id") String field,
                                                       @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                       @RequestBody AuthDepartmentInfoEntity authDepartmentInfoEntity) {
        ApiResult result;
        try {
            result = authDptService.queryDptInfoPinpoint(authDepartmentInfoEntity, page, pageSize, field, dir);
            return result;
        } catch (Exception e) {
            log.error("部门信息查询失败!", e);
            return ApiResult.fail("部门信息查询失败！");
        }
    }


    @LoginOpen
    @Log(module = ModuleType.SYSTEM_DEPART, action = BusinessType.DISABLED)
    @ApiOperation(value = "批量禁用部门", notes = "批量禁用部门")
    @RequestMapping(value = "/dptsStatus_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateDptsController(@RequestBody List<AuthDepartmentInfoEntity> dpts) {
        ApiResult result;
        try {
            result = authDptService.deleteDptsStatus(dpts);
            return result;
        } catch (Exception e) {
            log.error("删除部门失败!", e);
            return ApiResult.fail("删除部门失败！");
        }
    }

    @LoginOpen
    @ApiOperation(value = "校验部门是否存在表格/类目", notes = "校验部门是否存在表格/类目")
    @RequestMapping(value = "/checkDepExistTableOrCategory", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<?> checkDepController( @RequestParam(value = "depId") String depId) {
        ApiResult result;
        try {
            result = authDptService.checkDepExistTableOrCategory(depId);
            return result;
        } catch (Exception e) {
            log.error("校验部门失败!", e);
            return ApiResult.fail("校验部门失败！");
        }
    }
}
