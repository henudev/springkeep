package com.h3c.bigdata.zhgx.common.tokenSecurity.config.certification;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Jsr250SecurityConfig;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义投票器 增加loginOpen标签判断
 * @author w17193
 */
public class MyAuthenticatedVoter extends Jsr250Voter {

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> definition) {
        boolean jsr250AttributeFound = false;

        for (ConfigAttribute attribute : definition) {
            if (MyJsr250SecurityConfig.PERMIT_ALL_ATTRIBUTE.equals(attribute)) {
                return ACCESS_GRANTED;
            }

            /**
             * 非匿名 即必须登录成功后 才可以访问
             */
            if(!authenticationTrustResolver.isAnonymous(authentication)&&MyJsr250SecurityConfig.LOGIN_OPEN_ATTRIBUTE.equals(attribute)){
                return ACCESS_GRANTED;
            }

            if (Jsr250SecurityConfig.DENY_ALL_ATTRIBUTE.equals(attribute)) {
                return ACCESS_DENIED;
            }

            if (supports(attribute)) {
                jsr250AttributeFound = true;
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return jsr250AttributeFound ? ACCESS_DENIED : ACCESS_ABSTAIN;
    }
}
