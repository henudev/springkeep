package com.h3c.bigdata.zhgx.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: zhgx
 * @description: 解决联调跨域拦截器
 * @author: h17338
 * @create: 2018-08-02 10:19
 **/
@Component
public class ProcessInterceptor implements HandlerInterceptor {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(ProcessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("=====preHandle=====");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");

        httpServletResponse.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS,PATCH");

        httpServletResponse.setHeader("X-Powered-By","Jetty");


        String method= httpServletRequest.getMethod();

        if (method.equals("OPTIONS")){
            httpServletResponse.setStatus(200);
            return false;
        }
        log.info(method);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
