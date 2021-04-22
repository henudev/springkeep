package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserPwdBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthPasswordService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: h17338
 * @Description: 密码维护控制器
 * @Date: 2018/8/2
 */
@RestController
@RequestMapping(value = "/auth")
@LoginOpen
public class AuthPasswordController extends BaseController {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthPasswordController.class);

    @Autowired
    private AuthPasswordService authPasswordService;

    @LoginOpen
    @Log(module = ModuleType.SYSTEM_USER,action = BusinessType.PASSWD_RESET)
    @ApiOperation(value = "管理员重置用户密码", notes = "管理员重置用户密码")
    @RequestMapping(value = "/initialPassword_update", method = RequestMethod.PATCH)
    @ResponseBody
    public ApiResult<?> updateInitialPasswordController(@RequestBody AuthPasswordEntity passwordEntity) {
        try{
            return authPasswordService.updateUserInitialPassword(passwordEntity,getToken());
        } catch (Exception e){
            log.error("密码初始化失败!",e);
            return ApiResult.fail("密码初始化失败！");
        }
    }

    @LoginOpen
    @Log(module = ModuleType.SYSTEM_USER,action = BusinessType.PASSWD_MODIFY)
    @ApiOperation(value = "用户个人修改密码", notes = "用户个人修改密码")
    @RequestMapping(value = "/password_update", method = RequestMethod.POST, produces={"application/json"})
    @ResponseBody
    public ApiResult<?> updatePswByUserController(@RequestBody UserPwdBean userPwdBean) {
        try{
            userPwdBean.setUserId(getUserIdByToken());
            return authPasswordService.updatePswByUser(userPwdBean,getToken());
        } catch (Exception e){
            log.error("密码修改失败!",e);
            return ApiResult.fail("密码修改失败！");
        }
    }

}
