package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.model.UserWithUserRoleListModel;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserRoleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: zhgx
 * @description: 用户角色关联关系控制器
 * @author: h17338
 * @create: 2018-07-31 15:30
 **/
@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthUserRoleController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthUserRoleController.class);

    @Autowired
    private AuthUserRoleService authUserRoleService;

    @Log(module = ModuleType.SYSTEM_USER, action = BusinessType.INSERT)
    @ApiOperation(value = "新增用户时勾选角色列表", notes = "新增用户时勾选角色列表（操作用户、用户角色表）")
    @RequestMapping(value = "/userWithRoleList_insert", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> insertUserWithRoleListController(@RequestBody UserWithUserRoleListModel userWithUserRoleListModel) {
        ApiResult result;
        try {
            return authUserRoleService.addUseWithRoleList(userWithUserRoleListModel, getToken());
        } catch (Exception e) {
            log.error("新增用户失败!", e);
            return ApiResult.fail("新增用户失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_USER, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更用户时勾选角色列表", notes = "变更用户时勾选角色列表（操作用户、用户角色表）")
    @RequestMapping(value = "/userWithRoleList_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateUserWithRoleListController(@RequestBody UserWithUserRoleListModel userWithRoleListModel) {
        ApiResult result;
        try {
            result = authUserRoleService.updateUseWithRoleList(userWithRoleListModel);
            return result;
        } catch (Exception e) {
            log.error("编辑用户失败!", e);
            return ApiResult.fail("编辑用户失败！");
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @Description: 查询用户时返回角色关联关系信息（用户、用户角色、角色三表关联查询）
     * @Author: w15112
     * @CreateDate: 2018/11/27 13:44
     */
    @ApiOperation(value = "查询用户时返回角色关联关系信息", notes = "查询用户时返回角色关联关系信息（用户、用户角色、角色三表关联查询）")
    @RequestMapping(value = "/userWithRoleList_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryUserWithRoleListController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(value = "field", defaultValue = "id") String field,
                                                        @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                        @RequestBody UserWithRoleModel userWithRoleModel,
                                                        HttpServletRequest request) {
        ApiResult result;

        String userId = getUserIdByToken();
        try {
            result = authUserRoleService.queryUseWithRoleList(page, pageSize, field, dir, userWithRoleModel, userId);
            return result;
        } catch (Exception e) {
            log.error("查询用户失败!", e);
            return ApiResult.fail("查询用户失败！");
        }
    }

    /**
     * 方法描述 只要登陆就可以访问该接口，如果登陆时就返回用户头像等信息，这里可以配置为个人资料修改页面专用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "查询用户时返回角色关联关系信息-个人资料修改专用", notes = "查询用户时返回角色关联关系信息（用户、用户角色、角色三表关联查询）")
    @RequestMapping(value = "/userWithRoleList_query_for_person", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryUserWithRoleListForPersonController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                 @RequestParam(value = "field", defaultValue = "id") String field,
                                                                 @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                                 @RequestBody UserWithRoleModel userWithRoleModel,
                                                                 HttpServletRequest request) {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            userWithRoleModel.setUserId(userId);
            result = authUserRoleService.queryUseWithRoleListForPerson(page, pageSize, field, dir, userWithRoleModel);
            return result;
        } catch (Exception e) {
            log.error("查询用户失败!", e);
            return ApiResult.fail("查询用户失败！");
        }
    }

    @ApiOperation(value = "用户调用方名称/手机号校验", notes = "用户调用方名称/手机号校验")
    @RequestMapping(value = "/checkCallerNameOrPhone", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<?> checkCallerNameOrPhoneIsUsed( @RequestParam(value = "callerOrPhone") String callerOrPhone,
                                                      @RequestParam(value = "type") String type) {
        try {
            return authUserRoleService.checkCallerNameOrPhoneIsUsed(callerOrPhone,type);
        } catch (Exception e) {
            log.error("用户调用方名称/手机号校验失败!", e);
            return ApiResult.fail("用户调用方名称/手机号校验失败！");
        }
    }

}
