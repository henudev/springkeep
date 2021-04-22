package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.CollectBean;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping(value = "/auth")

public class AuthUserController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthUserController.class);

    @Autowired
    private AuthUserService authUserService;

    @LoginOpen
    @Log(module = ModuleType.SYSTEM_USER, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更用户信息", notes = "变更用户信息")
    @RequestMapping(value = "/userInfo_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateUserInfoController(@RequestBody AuthUserInfoEntity authUserInfoEntity) {
        ApiResult result;
        try {
            authUserInfoEntity.setUserId(getUserIdByToken());
            result = authUserService.updateUserInfo(authUserInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("变更用户信息失败!", e);
            return ApiResult.fail("变更用户信息失败！");
        }
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @RequestMapping(value = "/userInfo_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryUserInfoController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "field", defaultValue = "id") String field,
                                                @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                @RequestBody AuthUserInfoEntity authUserInfoEntity) {
        ApiResult result;
        try {
            result = authUserService.queryUserInfo(page, pageSize, field, dir, authUserInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("查询用户信息失败!", e);
            return ApiResult.fail("查询用户信息失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_USER, action = BusinessType.DELETE)
    @ApiOperation(value = "批量删除用户账号", notes = "批量停用用户账号")
    @RequestMapping(value = "/delete_users", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> updateUsersStatusController(@RequestBody UsersStatusBean usersStatusBean) {
        ApiResult result;
        try {
            result = authUserService.deleteUser(usersStatusBean);
            return result;
        } catch (Exception e) {
            log.error("删除用户失败!", e);
            return ApiResult.fail("删除用户失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_USER, action = BusinessType.UPDATE_STATUS)
    @ApiOperation(value = "禁用/启用户账号", notes = "禁用/启用用户账号")
    @RequestMapping(value = "/usersStatus_update", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> updateUsersStatus(@RequestBody UsersStatusBean usersStatusBean) {
        ApiResult result;
        try {
            result = authUserService.updateUsersStatus(usersStatusBean);
            return result;
        } catch (Exception e) {
            log.error("停用用户账号失败!", e);
            if ("0".equals(usersStatusBean.getStatus())) {
                return  ApiResult.success("用户启用失败！");
            } else {
                return  ApiResult.fail("用户禁用失败！");
            }
        }
    }


    @ApiOperation(value = "查询用户关联部门信息", notes = "查询用户关联部门信息")
    @RequestMapping(value = "/queryUserByRoleId", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryUserByRoleId(@RequestParam("roleId") String roleId) {
        return authUserService.queryUsersByRoleId(roleId);
    }


    @ApiOperation(value = "查询部门下的用户", notes = "查询部门下的用户")
    @RequestMapping(value = "/getPersonsByDeptId", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> getPersonsByDeptId(@RequestParam("deptId") String deptId) {
        return authUserService.getPersonsByDeptId(deptId);
    }


    @ApiOperation(value = "查询某用户所在部门的所有人员", notes = "查询某用户所在部门的所有人员")
    @RequestMapping(value = "/getPersonsByUserId", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> getPersonsByUserId() {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authUserService.getPersonsByUserId(userId);
            return result;
        } catch (Exception e) {
            log.error("查询某用户所在部门的所有人员失败!", e);
            return ApiResult.fail("查询某用户所在部门的所有人员失败！");
        }
    }

    @PermitAll
    @ApiOperation(value = "更新用户首次登陆标志位", notes = "更新用户首次登陆标志位")
    @RequestMapping(value = "/updateUserLoginFlag", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> updateUserLoginFlag(@RequestBody CollectBean collectBean) {
        ApiResult result;
        try {
            result = authUserService.updateUserLoginFlag(collectBean.getId());
            return result;
        } catch (Exception e) {
            log.error("更新用户是否首次登陆标志位失败", e);
            return ApiResult.fail("更新用户是否首次登陆标志位失败！");
        }
    }
/*
    @PermitAll
    @ApiOperation(value = "更新用户首次登陆标志位", notes = "更新用户首次登陆标志位")
    @RequestMapping(value = "/testUser", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> testUser(@RequestBody AuthUserInfo userInfo) {
       if(userInfo.getUserName().equals("yxg")&&userInfo.getSex().equals("nan")){
           return ApiResult.success("登录成功");
       }else{
           return ApiResult.fail("失败啦");
       }*/

   // }
}
