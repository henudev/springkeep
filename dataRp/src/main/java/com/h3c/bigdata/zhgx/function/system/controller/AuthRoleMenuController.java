package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuListModel;
import com.h3c.bigdata.zhgx.function.system.service.AuthRoleMenuService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: zhgx
 * @description: 角色权限关联关系控制器
 * @author: h17338
 * @create: 2018-07-31 15:49
 **/
@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthRoleMenuController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthRoleMenuController.class);

    @Autowired
    private AuthRoleMenuService authRoleMenuService;


    /**
     * @param
     * @return
     * @throws
     * @Description: 获取角色类型列表方法
     * @Author: w15112
     * @CreateDate: 2018/11/27 14:04
     */
    @Log(module = ModuleType.SYSTEM_ROLE_MENU, action = BusinessType.INSERT)
    @ApiOperation(value = "获取角色类型列表方法", notes = "获取角色类型列表方法")
    @RequestMapping(value = "/getRoleTypeList", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> getRoleTypeList() {
        ApiResult result;
        String userId = getUserIdByToken();
        try {
            result = authRoleMenuService.getRoleTypeList(userId);
            return result;
        } catch (Exception e) {
            log.error("获取角色类型列表方法!", e);
            return ApiResult.fail("获取角色类型列表方法！");
        }
    }


    @Log(module = ModuleType.SYSTEM_ROLE_MENU, action = BusinessType.INSERT)
    @ApiOperation(value = "新增角色勾选权限关联关系信息", notes = "新增角色勾选权限关联关系信息（入角色、角色权限表）")
    @RequestMapping(value = "/roleWithMenuList_insert", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> insertWithMenuListController(@RequestBody RoleWithRoleMenuListModel roleWithRoleMenuListModel) {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authRoleMenuService.addRoleWithMenuList(roleWithRoleMenuListModel, userId);
            return result;
        } catch (Exception e) {
            log.error("新增角色权限关系失败!", e);
            return ApiResult.fail("新增角色权限关系失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_ROLE_MENU, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更角色权限关联关系信息", notes = "变更角色权限关联关系信息（入角色、角色权限表）")
    @RequestMapping(value = "/roleWithMenuList_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateRoleMenuInfoController(@RequestBody RoleWithRoleMenuListModel roleWithRoleMenuListModel) {
        ApiResult result;
        try {
            result = authRoleMenuService.updateRoleWithMenuList(roleWithRoleMenuListModel);
            return result;
        } catch (Exception e) {
            log.error("变更角色权限关系失败!", e);
            return ApiResult.fail("变更角色权限关系失败！");
        }
    }


    @ApiOperation(value = "查询角色和已勾选权限列表", notes = "查询角色和已勾选权限列表（用户、用户角色、角色三表关联查询）")
    @RequestMapping(value = "/roleWithMenuList_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryRoleWithMenuListController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(value = "field", defaultValue = "id") String field,
                                                        @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                        @RequestBody RoleWithRoleMenuBean roleWithRoleMenuBean) {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authRoleMenuService.queryRoleWithMenuList(page, pageSize, field, dir, roleWithRoleMenuBean, userId);
            return result;
        } catch (Exception e) {
            log.error("查询角色权限关系失败!", e);
            return ApiResult.fail("查询角色权限关系失败！");
        }
    }


    @ApiOperation(value = "流程管理时查询角色列表", notes = "流程管理时查询角色列表")
    @RequestMapping(value = "/queryRoleForApprove", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryRoleForApprove(@RequestBody RoleWithRoleMenuBean roleWithRoleMenuBean) {
        ApiResult result;
        try {
            result = authRoleMenuService.queryRoleForApprove(roleWithRoleMenuBean);
            return result;
        } catch (Exception e) {
            log.error("查询角色权限关系失败!", e);
            return ApiResult.fail("查询角色权限关系失败！");
        }
    }


}
