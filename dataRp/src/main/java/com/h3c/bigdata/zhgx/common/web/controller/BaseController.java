package com.h3c.bigdata.zhgx.common.web.controller;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.tokenSecurity.util.JwtTokenUtil;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取token中的登录信息
     *
     * @return
     */
    public String getUserIdByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        String userId = null;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(authToken);
            if (null != userDetails) {
                userId = userDetails.getUsername();
            }
        }
        return userId;
    }

    /**
     * 获取token信息
     *
     * @param
     * @return
     */
    public String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        String authToken = "";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            authToken = authHeader.substring(tokenHead.length());
        }
        return authToken;
    }

    /**
     * 清除token信息
     *
     * @param
     * @return
     */
    public void cleanToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            LoginUtil.removeCacheByToken(authToken);
        }

    }
}
