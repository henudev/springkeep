package com.h3c.bigdata.zhgx.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @program: zhgx
 * @description: 拦截器链
 * @author: h17338
 * @create: 2018-12-14 15:30
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ProcessInterceptor pocessInterceptor;

    @Autowired
    private RefTokenInterceptor refTokenInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(pocessInterceptor);

        registry.addInterceptor(refTokenInterceptor);
    }

}
