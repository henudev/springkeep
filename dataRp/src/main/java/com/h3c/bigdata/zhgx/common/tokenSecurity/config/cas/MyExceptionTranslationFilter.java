package com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * token超期 重登陆处理
 *
 * @author w17193
 */
@Component
@Slf4j
public class MyExceptionTranslationFilter extends ExceptionTranslationFilter {

    public MyExceptionTranslationFilter(CasAuthenticationEntryPoint casAuthenticationEntryPoint) {
        super(casAuthenticationEntryPoint);
    }

    @Override
    public void sendStartAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            AuthenticationException reason)
            throws ServletException, IOException {

        SecurityContextHolder.getContext().setAuthentication(null);

        String authToken = null;
        // 清空缓存
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            authToken = authHeader.substring(tokenHead.length());
            LoginUtil.removeCacheByToken(authToken);
        }

        String oldUserName = null;
        List<String> validTokens = new ArrayList<>();

        if (!CaCheMapConst.USERTICKETCACHE.isEmpty() && CaCheMapConst.USERLOUGOUTCHAHE.size() != 0) {
            oldUserName = CaCheMapConst.USERLOUGOUTCHAHE.asMap().get(authToken);
            if (null != oldUserName){
                validTokens = CaCheMapConst.USERTICKETCACHE.get(oldUserName);
            }

        }

        // 非options请求 进行回显
        if (!request.getMethod().equals(HttpMethod.OPTIONS)) {
            // 未登录460 token超期450 用户在其他地方登陆440
            if (StringUtils.isEmpty(authHeader)) {
                response.setStatus(460);
            } else if ((null !=validTokens)&&!validTokens.isEmpty() && !oldUserName.isEmpty()
                      && !validTokens.contains(authToken)) {
                response.setStatus(440);
            } else {
                response.setStatus(450);
            }

            response.setHeader("Access-Control-Allow-Origin", "*");

            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");

            response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS,PATCH");

            response.setHeader("X-Powered-By", "Jetty");

            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().print("error!!!");

        }
    }
}
