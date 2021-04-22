package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.service.AuthRoleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthRoleController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthRoleController.class);

    @Autowired
    private AuthRoleService authRoleService;

    @ApiOperation(value = "查询角色信息", notes = "查询角色信息")
    @RequestMapping(value = "/roleInfo_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryRoleInfoController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "field", defaultValue = "id") String field,
                                                @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                @RequestBody AuthRoleInfoEntity authRoleInfoEntity) {
        ApiResult result;
        try {
            result = authRoleService.queryRoleInfo(page, pageSize, field, dir, authRoleInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("查询角色失败!", e);
            return ApiResult.fail("查询角色失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_ROLE, action = BusinessType.BATCH_DELETE)
    @ApiOperation(value = "批量删除角色信息", notes = "批量删除角色信息")
    @RequestMapping(value = "/roles_delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResult<?> deleteRolesController(@RequestBody List<AuthRoleInfoEntity> roles) {
        ApiResult result;
        try {
            result = authRoleService.deleteRoles(roles);
            return result;
        } catch (Exception e) {
            log.error("批量删除角色失败!", e);
            return ApiResult.fail("批量删除角色失败！");
        }
    }
}
