package com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义cas拦截器
 * 实现针对ticket的操作处理
 * @author w17193
 */
public class MyCasAuthenticationFilter extends CasAuthenticationFilter {

    private String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;

    @Override
    protected String obtainArtifact(HttpServletRequest request) {
        //登录成功后会返回ticket 获取后可以跳转到 登录成功方法
        String ticket = request.getParameter(artifactParameter);
        if(ticket==null){
            String authHeader = request.getHeader("Authorization");
            String tokenHead = "Bearer ";

            if (authHeader != null && authHeader.startsWith(tokenHead)) {
                String authToken = authHeader.substring(tokenHead.length());
                UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(authToken);
                if(userDetails != null){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    ticket = authToken;
                }
            }else{
                SecurityContext context = SecurityContextHolder.getContext();
                //设置为匿名用户
                AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("key", "anonymous",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
                context.setAuthentication(anonymousAuthenticationToken);
            }
        }

        return ticket;
    }


}
