package com.h3c.bigdata.zhgx.common.interceptor;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 引用监听器 监听controller执行情况 执行结束后 刷新token时间
 * @author w17193
 */
@Configuration
public class RefTokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //刷新一下当前登录的token
        String authHeader = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        String authToken = "";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            authToken = authHeader.substring(tokenHead.length());
            UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(authToken);
            if(userDetails!=null){
                CaCheMapConst.USERCACHE.put(authToken,userDetails);
            }
        }


        super.afterCompletion(request, response, handler, ex);
    }
}
