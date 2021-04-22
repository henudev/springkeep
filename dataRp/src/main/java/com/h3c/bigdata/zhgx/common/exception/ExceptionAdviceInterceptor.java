package com.h3c.bigdata.zhgx.common.exception;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.concurrent.TimeUnit;

/**
 * @Author: h17338
 * @Description: 基于AOP的全局异常统一处理
 * @Date: 2018/7/18
 *
 * 修改异常获取和请求
 * @author w17193
 * @date 2018/11/17
 */
@Aspect
@Component
public class ExceptionAdviceInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(ExceptionAdviceInterceptor.class);

    @Around("execution(* com.h3c.bigdata.zhgx..controller..*.*(..)) && !execution(* com.h3c.bigdata.zhgx.function.report.controller.*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        ApiResult<?> ApiResult;
        try {
            logger.info("执行Controller开始: " + pjp.getSignature() + " 参数：" + Lists.newArrayList(pjp.getArgs()).toString());
            ApiResult = (ApiResult<?>) pjp.proceed(pjp.getArgs());
            String returnStr = null;
            if(ApiResult!=null){
                returnStr = ApiResult.toString();
            }
            logger.info("执行Controller结束: " + pjp.getSignature() + "， 返回值：" + returnStr);
            logger.info("耗时：" + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + "(毫秒).");
        } catch (Throwable throwable) {
            ApiResult = handlerException(pjp, throwable);
        }

        return ApiResult;
    }

    private ApiResult<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ApiResult<?> apiResult = null;
        if(e.getClass().isAssignableFrom(ComBaseException.class) ){
            ComBaseException comBaseException = (ComBaseException)e;
            logger.error("RuntimeException{方法：" + pjp.getSignature() + "， 参数：" + pjp.getArgs() + ",异常：" + comBaseException.getErrorCode() + "}", e);
            apiResult = new ApiResult(comBaseException.getErrorCode(),"","");
        } else if (e instanceof RuntimeException) {
            logger.error("RuntimeException{方法：" + pjp.getSignature() + "， 参数：" + pjp.getArgs() + ",异常：" + e.getMessage() + "}", e);
            apiResult = new ApiResult(ComErrorCode.SYS_ERROR_000000.getErrorCode(),"","");
        } else {
            logger.error("异常{方法：" + pjp.getSignature() + "， 参数：" + pjp.getArgs() + ",异常：" + e.getMessage() + "}", e);
            apiResult = new ApiResult(ComErrorCode.SYS_ERROR_000000.getErrorCode(),"","");
        }

        return apiResult;
    }
}
