package com.h3c.bigdata.zhgx.common.tokenSecurity.login;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas.CasProperties;
import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;
import com.h3c.bigdata.zhgx.function.system.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录成功需要执行 将信息放入缓存 同时返回前端地址
 *
 * @author w17193
 */
@Configuration
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired private ILoginLogService loginLogService;

  @Autowired private CasProperties acmCasProperties;

  @Value("${login.only}")
  private Boolean loginOnly;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token = authentication.getCredentials().toString();

    List<String> tokenArr = CaCheMapConst.USERTICKETCACHE.get(userDetails.getUsername());

    if (tokenArr == null || tokenArr.size() == 0) {
      tokenArr = new ArrayList<>();
      tokenArr.add(token);
    } else {
      if (loginOnly) {
        // 将缓存中的token 失效
        String oldToken = tokenArr.get(0);
        // 将待删除token信息存入
        CaCheMapConst.USERLOUGOUTCHAHE.put(oldToken,userDetails.getUsername());
        CaCheMapConst.USERCACHE.invalidate(oldToken);

        tokenArr = new ArrayList<>();
        tokenArr.add(token);
      } else {
        tokenArr.add(token);
      }
    }
      request.getRemoteAddr();

    CaCheMapConst.USERTICKETCACHE.put(userDetails.getUsername(), tokenArr);

    CaCheMapConst.USERCACHE.put(token, userDetails);

    response.sendRedirect(acmCasProperties.getWebUrl() + "?token=" + token);

    //在登录日志中插入相应的记录
    insertLoginLog(userDetails.getUsername(),request.getRemoteAddr());

  }

  public void insertLoginLog(String userName, String clientIp) {
    LoginLog loginLog = new LoginLog();
    loginLog.setIpAddress(clientIp);
    loginLog.setLoginName(userName);
    // 登录成功：0-成功、1-失败
    loginLog.setStatus("0");
    loginLog.setMsg("登录成功！");
    loginLogService.insertLoginLog(loginLog);
  }
}
