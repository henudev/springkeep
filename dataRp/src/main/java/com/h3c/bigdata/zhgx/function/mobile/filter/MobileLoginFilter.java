package com.h3c.bigdata.zhgx.function.mobile.filter;

import com.alibaba.fastjson.JSONObject;
import com.h3c.bigdata.zhgx.common.tokenSecurity.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/mobile_application/*", filterName = "mobileLoginFilter")
@Component
@Slf4j
public class MobileLoginFilter implements Filter {

    final JwtTokenUtil jwtTokenUtil;

    public static final String AUTH = "Mobile-Authorization";
    public static final String TOKEN_HEADER = "Bearer ";
    private static final String MOBILE_LOGIN_URL = "/mobile_application/login";

    public MobileLoginFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader(AUTH);
        String url = request.getServletPath();
        if (!MOBILE_LOGIN_URL.equals(url)) {
            JSONObject check = new JSONObject();
            if (StringUtils.isNotBlank(token) && token.startsWith(TOKEN_HEADER)) {
                token = token.substring(TOKEN_HEADER.length());
                if (!jwtTokenUtil.isTokenExpired(token)) {
                    check.put("message", "登陆过期，请重新登录");
                    check.put("code", "1002");
                    writeResponse(response, check);
                    return;
                }
            } else {
                check.put("message", "您尚未登录,请先登录后");
                check.put("code", "1001");
                writeResponse(response, check);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void writeResponse(HttpServletResponse response, JSONObject jsonObject) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(jsonObject.toJSONString());
        writer.flush();
    }

    @Override
    public void destroy() {
    }
}
