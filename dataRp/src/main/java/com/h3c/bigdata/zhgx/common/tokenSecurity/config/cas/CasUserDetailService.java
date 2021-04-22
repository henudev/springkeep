package com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas;

import com.h3c.bigdata.zhgx.common.tokenSecurity.entity.JwtUser;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.LoginMenuBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthMenuService;
import com.h3c.bigdata.zhgx.function.system.serviceImpl.AuthMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 登录成功后，进行用户的授权
 * @author w17193
 */
@Component
@Slf4j
public class CasUserDetailService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Autowired
    private AuthMenuService authMenuService;

    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        log.info("校验成功的登录名为: " + token.getName());

        List<LoginMenuBean> loginMenuBeans =  authMenuService.queryLoginMenuByUserId(token.getName());
                //查询当前用户的权限url列表
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(LoginMenuBean loginMenuBean : loginMenuBeans){
            String menuCode = loginMenuBean.getMenuCode();

            String functionType =loginMenuBean.getFunctionType();

            if(!StringUtils.isEmpty(functionType)){

                String[] functionTypeArr = functionType.split(",");

                for(String functionTypeStr : functionTypeArr){
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+menuCode+"_"+functionTypeStr));
                }
            }

            authorities.add(new SimpleGrantedAuthority("ROLE_"+menuCode));

        }

        return new JwtUser(token.getName(),authorities);
    }

}