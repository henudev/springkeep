package com.h3c.bigdata.zhgx.common.tokenSecurity.config.certification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * jsr250的标签启用类
 * 同时增加自定义投票器 和 自定义标签启动时 的扫描类
 * @author w17193
 */
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class MyGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    public void setJsr250MethodSecurityMetadataSource(MyJsr250MethodSecurityMetadataSource jsr250MethodSecurityMetadataSource) {
        super.setJsr250MethodSecurityMetadataSource(jsr250MethodSecurityMetadataSource);
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());

        //prePostEnabled == true
        //decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));

        //jsr250Enabled == true
        decisionVoters.add(new MyAuthenticatedVoter());

        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        return new AffirmativeBased(decisionVoters);
    }
}
