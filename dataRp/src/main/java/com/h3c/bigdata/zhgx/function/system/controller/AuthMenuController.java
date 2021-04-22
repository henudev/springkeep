package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.AuthMenuBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthMenuService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: h17338
 * @Description: 权限维护控制器
 * @Date: 2018/7/31
 */
@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthMenuController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthMenuController.class);

    @Autowired
    private AuthMenuService authMenuService;


    @Log(module = ModuleType.SYSTEM_MENU, action = BusinessType.INSERT)
    @ApiOperation(value = "新增菜单(权限)信息", notes = "新增菜单(权限)信息")
    @RequestMapping(value = "/menuInfo_insert", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> insertMenuInfoController(@RequestBody AuthMenuBean authMenuBean) {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authMenuService.addMenuInfo(authMenuBean, userId);
            return result;
        } catch (Exception e) {
            log.error("新增权限信息失败！", e);
            return ApiResult.fail("新增权限信息失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_MENU, action = BusinessType.UPDATE)
    @ApiOperation(value = "变更权限信息", notes = "变更权限信息")
    @RequestMapping(value = "/menuInfo_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateMenuInfoController(@RequestBody AuthMenuBean authMenuBean) {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authMenuService.updateMenuInfo(authMenuBean, userId);
            return result;
        } catch (Exception e) {
            log.error("权限基本信息更新失败！", e);
            return ApiResult.fail("权限基本信息更新失败！");
        }
    }


    @ApiOperation(value = "查询权限信息", notes = "查询权限信息")
    @RequestMapping(value = "/menuInfo_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryMenuInfoController(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "field", defaultValue = "id") String field,
                                                @RequestParam(value = "dir", defaultValue = "asc") String dir,
                                                @RequestBody AuthMenuInfoEntity authMenuInfoEntity) {
        ApiResult result;
        try {
            result = authMenuService.queryMenuInfo(page, pageSize, field, dir, authMenuInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("查询权限信息失败！", e);
            return ApiResult.fail("查询权限信息失败！");
        }
    }


    @ApiOperation(value = "查询权限信息", notes = "查询权限信息")
    @RequestMapping(
            value = "/menuInfo_queryPinpoint",
            method = RequestMethod.POST,
            produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryMenuInfoPinpointController(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "field", defaultValue = "id") String field,
            @RequestParam(value = "dir", defaultValue = "asc") String dir,
            @RequestBody AuthMenuInfoEntity authMenuInfoEntity) {
        ApiResult result;
        try {
            result = authMenuService.queryMenuInfoPinpoint(page, pageSize, field, dir, authMenuInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("查询权限信息失败！", e);
            return ApiResult.fail("查询权限信息失败！");
        }
    }


    @Log(module = ModuleType.SYSTEM_MENU, action = BusinessType.BATCH_DELETE)
    @ApiOperation(value = "删除权限信息", notes = "删除权限信息")
    @RequestMapping(value = "/menus_delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResult<?> deleteMenusController(@RequestBody List<AuthMenuInfoEntity> menus) {
        ApiResult result;
        try {
            result = authMenuService.deleteMenus(menus);
            return result;
        } catch (Exception e) {
            log.error("菜单删除失败！", e);
            return ApiResult.fail("菜单删除失败！");
        }
    }


    @ApiOperation(value = "查询全量菜单树", notes = "查询全量菜单树")
    @RequestMapping(value = "/menuTree_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryMenuTreeController(@RequestBody AuthMenuInfoEntity authMenuInfoEntity) {
        ApiResult result;
        try {
            result = authMenuService.queryMenuTree(authMenuInfoEntity);
            return result;
        } catch (Exception e) {
            log.error("菜单树查询失败！", e);
            return ApiResult.fail("菜单树查询失败！");
        }
    }

    @ApiOperation(value = "获取父级菜单分类列表", notes = "获取父级菜单分类列表")
    @RequestMapping(value = "/getMenuCategoryList", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> getMenuCategoryList() {
        ApiResult result;
        try {
            result = authMenuService.getMenuCategoryList();
            return result;
        } catch (Exception e) {
            log.error("获取父级菜单分类列表失败！", e);
            return ApiResult.fail("获取父级菜单分类列表失败！");
        }
    }

    //登录成功后 获取用户菜单权限
    @ApiOperation(value = "根据登录账号查询菜单树", notes = "根据登录账号查询菜单树")
    @RequestMapping(value = "/menuTreeByUserId_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryMenuTreeByUserIdController() {
        ApiResult result;
        try {
            result = authMenuService.queryMenuTreeByUserId(getUserIdByToken());
            return result;
        } catch (Exception e) {
            log.error("根据登录账号查询菜单树失败！", e);
            return ApiResult.fail("根据登录账号查询菜单树失败!");
        }
    }

    //公共资源免密跳转
    @ApiOperation(value = "根据登录账号获取PAAS首页免密跳转地址", notes = "根据登录账号获取PAAS首页免密跳转地址")
    @RequestMapping(value = "/paasUrlByUserId_query", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryPaasUrlByUserIdController(@RequestParam String userId) throws Exception {
        ApiResult result;
        try {
            result = authMenuService.queryPaasUrlByUserId(userId);
            return result;
        } catch (Exception e) {
            log.error("暂时无法访问，请联系管理员！", e);
            return ApiResult.fail("暂时无法访问，请联系管理员!");
        }
    }

    @ApiOperation(value = "根据菜单主键获取菜单信息（包括按钮及URL信息）", notes = "根据菜单主键获取菜单信息（包括按钮及URL信息）")
    @RequestMapping(value = "/menuDetailById_query", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> queryMenuButtonByMenuId(@RequestParam String id) {
        try {
            AuthMenuBean authMenuBean = authMenuService.queryMenuButtonByMenuCode(id);
            return ApiResult.success("菜单信息获取成功！", authMenuBean);
        } catch (Exception e) {
            log.error("菜单信息获取失败!", e);
            return ApiResult.fail("菜单信息获取失败！");
        }
    }

    @Log(module = ModuleType.SYSTEM_ROLE_MENU, action = BusinessType.INSERT)
    @ApiOperation(value = "新增角色时获取菜单列表", notes = "新增角色时获取菜单列表")
    @RequestMapping(value = "/getMenuList", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ApiResult<?> getMenuList() {
        ApiResult result;
        try {
            String userId = getUserIdByToken();
            result = authMenuService.getMenuList(userId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增角色时获取菜单列表失败!", e);
            return ApiResult.fail("新增角色时获取菜单列表失败！");
        }
    }
}
