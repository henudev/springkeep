package com.h3c.bigdata.zhgx.common.tokenSecurity.config.certification;

import com.alibaba.fastjson.JSON;
import com.h3c.bigdata.zhgx.common.exception.ComErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定403返回值
 * @author w17193
 * */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
    try {
      response.setHeader("Access-Control-Allow-Origin", "*");
      Map<String, Object> jsonMap = new HashMap<>();
      jsonMap.put("message",ComErrorCode.LOGIN_ERROR_100006.getErrorMsg());
      jsonMap.put("data",ComErrorCode.LOGIN_ERROR_100006.getMsgDetail());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(403);
      response.getWriter().print(JSON.toJSONString(jsonMap));
      response.getWriter().close();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }
}
