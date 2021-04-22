package com.h3c.bigdata.zhgx.function.report.config;


import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import com.h3c.bigdata.zhgx.function.report.base.BusinessException;
import com.h3c.bigdata.zhgx.function.report.base.ParamJsonException;
import com.h3c.bigdata.zhgx.function.report.base.PublicResultConstant;
import com.h3c.bigdata.zhgx.ws.auth.InvalidAuthTokenException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller统一异常处理
 *
 * @author : sunleilei
 * @date : 2018/05/08
 */
@ControllerAdvice
public class AllControllerAdvice {
    private static Logger logger = LoggerFactory.getLogger(AllControllerAdvice.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     */
    @ModelAttribute
    public void addAttributes(Model model) {
    }

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseModel<String> errorHandler(Exception ex) {
        ex.printStackTrace();
        logger.error("接口出现严重异常：{}", ex.getMessage());
        return ResponseHelper.validationFailure(PublicResultConstant.FAILED);
    }
    /**
     * 捕捉BusinessException自定义抛出的异常
     *
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseModel handleBusinessException(BusinessException e) {
        if (e instanceof BusinessException) {
            logger.info("数据操作失败：" + e.getMessage());
            return ResponseHelper.validationFailure(e.getMessage());
        }
        return ResponseHelper.validationFailure(PublicResultConstant.ERROR);
    }

    /**
     * 捕捉UnauthorizedException
     *
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseModel<String> handleUnauthorized() {
        return ResponseHelper.validationFailure(PublicResultConstant.USER_NO_PERMITION);
    }

    /**
     * 捕捉shiro的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public ResponseModel<String> handleShiroException(ShiroException e) {
        return ResponseHelper.validationFailure(PublicResultConstant.USER_NO_PERMITION);
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(TemplateInputException.class)
    @ResponseBody
    public ResponseModel<String> handleTemplateInputException(TemplateInputException e) {
        return ResponseHelper.validationFailure(PublicResultConstant.USER_NO_PERMITION);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = ParamJsonException.class)
    @ResponseBody
    public ResponseModel<String> handleParamJsonException(Exception e) {
        if (e instanceof ParamJsonException) {
            logger.info("参数错误：" + e.getMessage());
            return ResponseHelper.validationFailure("参数错误：" + e.getMessage());
        }
        return ResponseHelper.validationFailure(PublicResultConstant.ERROR);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public HttpServletResponse handleParamAccessDeniedException(Exception e, HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                FilterChain chain,
                                                                AuthenticationException reason)
            throws ServletException, IOException {
        if (e instanceof AccessDeniedException) {
            SecurityContextHolder.getContext().setAuthentication(null);

            String authToken = null;
            // 清空缓存
            String authHeader = request.getHeader("Authorization");
            String tokenHead = "Bearer ";
            if (authHeader != null && authHeader.startsWith(tokenHead)) {
                authToken = authHeader.substring(tokenHead.length());
                LoginUtil.removeCacheByToken(authToken);
            }

            String oldUserName = null;
            List<String> validTokens = new ArrayList<>();

            if (!CaCheMapConst.USERTICKETCACHE.isEmpty() && CaCheMapConst.USERLOUGOUTCHAHE.size() != 0) {
                oldUserName = CaCheMapConst.USERLOUGOUTCHAHE.asMap().get(authToken);
                if (null != oldUserName){
                    validTokens = CaCheMapConst.USERTICKETCACHE.get(oldUserName);
                }

            }

            // 非options请求 进行回显
            if (!request.getMethod().equals(HttpMethod.OPTIONS)) {
                // 未登录460 token超期450 用户在其他地方登陆440
                if (StringUtils.isEmpty(authHeader)) {
                    response.setStatus(460);
                } else if ((null !=validTokens)&&!validTokens.isEmpty() && !oldUserName.isEmpty()
                        && !validTokens.contains(authToken)) {
                    response.setStatus(440);
                } else {
                    response.setStatus(450);
                }

                response.setHeader("Access-Control-Allow-Origin", "*");

                response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");

                response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS,PATCH");

                response.setHeader("X-Powered-By", "Jetty");

                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().print("error!!!");

            }

        }
        return response;
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = InvalidAuthTokenException.class)
    @ResponseBody
    public HttpServletResponse handleParamAccessDeniedException(HttpServletResponse response){
        response.setStatus(440);
        return response;
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(OfficeXmlFileException.class)
    @ResponseBody
    public ResponseModel<String> handleTemplateInputException(OfficeXmlFileException e) {
        return ResponseHelper.validationFailure(PublicResultConstant.EXCEL_ERROR);
    }
}
