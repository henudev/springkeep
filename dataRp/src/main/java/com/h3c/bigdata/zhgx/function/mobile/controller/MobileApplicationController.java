package com.h3c.bigdata.zhgx.function.mobile.controller;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.mobile.model.LoginDTO;
import com.h3c.bigdata.zhgx.function.mobile.service.MobileLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile_application")
public class MobileApplicationController extends BaseController {

    private MobileLoginService mobileLoginService;

    public MobileApplicationController(MobileLoginService mobileLoginService) {
        this.mobileLoginService = mobileLoginService;
    }

    /**
     * 移动端登陆并返回数据
     *
     * @param
     * @return
     */
    @PostMapping("/login")
    public ApiResult getUserName(@RequestBody LoginDTO loginDTO) {
        return mobileLoginService.login(loginDTO, false);
    }
}
