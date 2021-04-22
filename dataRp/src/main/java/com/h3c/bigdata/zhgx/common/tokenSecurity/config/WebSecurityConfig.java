package com.h3c.bigdata.zhgx.common.tokenSecurity.config;

import com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas.CasProperties;
import com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas.MyCasAuthenticationFilter;
import com.h3c.bigdata.zhgx.common.tokenSecurity.login.AuthenticationSuccessHandler;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * spring security 统一入口
 * @author w17193
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 自定义过滤规则及其安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // HeadersConfigurer
        http.headers().frameOptions().disable();

        // CsrfConfigurer
        http.csrf().disable();

        // 入口
        CasAuthenticationEntryPoint entryPoint = getApplicationContext().getBean(CasAuthenticationEntryPoint.class);
        CasAuthenticationFilter casAuthenticationFilter = getApplicationContext()
                .getBean(CasAuthenticationFilter.class);
        SingleSignOutFilter singleSignOutFilter = getApplicationContext().getBean(SingleSignOutFilter.class);
        LogoutFilter logoutFilter = getApplicationContext().getBean(LogoutFilter.class);
        /**
         * 执行顺序为
         * LogoutFilter-->SingleSignOutFilter-->CasAuthenticationFilter-->
         * ExceptionTranslationFilter
         */
        http.exceptionHandling().authenticationEntryPoint(entryPoint).and().addFilter(casAuthenticationFilter)
                .addFilterBefore(logoutFilter, LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //放入cas凭证校验器
        AuthenticationProvider authenticationProvider = (AuthenticationProvider) getApplicationContext()
                .getBean("casProvider");
        auth.authenticationProvider(authenticationProvider);

    }

    /**
     * cas filter类
     *
     * 针对/login请求的校验
     *
     * @return
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties properties,
                                                           CasProperties acmCasProperties) throws Exception {
        MyCasAuthenticationFilter casAuthenticationFilter = new MyCasAuthenticationFilter();
        casAuthenticationFilter.setServiceProperties(properties);
        casAuthenticationFilter.setFilterProcessesUrl(acmCasProperties.getAppServiceLoginUrl());
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return casAuthenticationFilter;
    }
}
