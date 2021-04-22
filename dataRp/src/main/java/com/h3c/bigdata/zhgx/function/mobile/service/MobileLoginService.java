package com.h3c.bigdata.zhgx.function.mobile.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.mobile.model.LoginDTO;

public interface MobileLoginService {
    /**
     * 移动端登录系统
     * loginFlag为false，手机端用户密码登录；loginFlag为true，钉钉内h5免登录
     */
    ApiResult login(LoginDTO loginDTO, Boolean loginFlag);
}
