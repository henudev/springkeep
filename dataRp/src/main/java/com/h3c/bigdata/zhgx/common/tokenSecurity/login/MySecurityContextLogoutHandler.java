package com.h3c.bigdata.zhgx.common.tokenSecurity.login;

import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 当用户点击退出时候调用此方法
 *
 * @author w17193
 */
public class MySecurityContextLogoutHandler extends SecurityContextLogoutHandler {

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    // 清空缓存
    String authHeader = request.getHeader("Authorization");
    String tokenHead = "Bearer ";
    if (authHeader != null && authHeader.startsWith(tokenHead)) {
      String authToken = authHeader.substring(tokenHead.length());
      LoginUtil.removeCacheByToken(authToken);
    }
    super.logout(request, response, authentication);
  }
}
