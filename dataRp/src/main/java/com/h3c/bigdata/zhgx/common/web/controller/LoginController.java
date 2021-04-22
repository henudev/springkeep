package com.h3c.bigdata.zhgx.common.web.controller;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.exception.ComBaseException;
import com.h3c.bigdata.zhgx.common.exception.ComErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas.CasProperties;
import com.h3c.bigdata.zhgx.common.web.model.SsoRespBean;
import com.h3c.bigdata.zhgx.function.system.serviceImpl.AuthUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.HashMap;
import java.util.Map;

/**
 * @author w17193
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private AuthUserServiceImpl authUserServiceImpl;

    /**
     * 登陆后获取用户基本信息
     *
     * @param
     * @return
     */

    //@LoginOpen
    @RequestMapping("/getUserName")
    public ApiResult getUserName() throws ComBaseException {

        String token = getToken();
        if (StringUtils.isEmpty(token)) {
            throw new ComBaseException(ComErrorCode.LOGIN_ERROR_100001);
        }

        UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(token);

        if (userDetails == null) {
            throw new ComBaseException(ComErrorCode.LOGIN_ERROR_100001);
        }

        String userName = userDetails.getUsername();

        return authUserServiceImpl.queryLoginUser(userName);
    }

    @PermitAll
    @RequestMapping(value = "/getSsoUrl", method = RequestMethod.GET)
    public ApiResult getSsoUrl() {
        SsoRespBean ssoRespBean = new SsoRespBean();
        ssoRespBean.setLoginUrl(casProperties.getCasServerPrefix() + casProperties.getCasServerLoginUrl());
        ssoRespBean.setLogoutUrl(casProperties.getCasServerPrefix() + casProperties.getCasServerLogoutUrl());
        return ApiResult.success(ssoRespBean);
    }

    /**
     * 退出时清除本地用户token信息
     *
     * @param
     * @return
     */
    @LoginOpen
    @RequestMapping(value = "/cleanLocalToken", method = RequestMethod.POST)
    public ApiResult cleanLocalToken() {
        try {
           // cleanToken();
            return ApiResult.success("清空本地用户信息成功");
        } catch (Exception e) {
            return ApiResult.success("清空本地用户信息失败");
        }
    }

    /**
     * 检查用户是否登录
     *
     * @param userName
     * @return
     */
    @PermitAll
    @RequestMapping(value = "/checkLoginStatus", method = RequestMethod.GET)
    public ApiResult checkLoginStatus(@RequestParam("userName") String userName) {
        try {
            boolean flag = CaCheMapConst.USERTICKETCACHE.containsKey(userName);
            Map<String, Object> map = new HashMap<>();
            map.put("checkFlag", flag);
            return ApiResult.success("检查用户登录状态成功", map);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("检查用户登录状态失败");
        }
    }

}
