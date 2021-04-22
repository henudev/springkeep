package com.h3c.bigdata.zhgx.common.log;

import com.h3c.bigdata.zhgx.common.constant.BusinessStatus;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.OperateLog;
import com.h3c.bigdata.zhgx.function.system.service.IOperateLogService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志记录处理.
 *
 * @Author J16898
 * @Date 2018/7/31
 * @Version 1.0
 */
@Aspect
@Component
@EnableAsync
public class LogAspect extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private IOperateLogService operateLogService;

    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;

    //配置织入点
    @Pointcut("@annotation(com.h3c.bigdata.zhgx.common.log.Log)")
    public void logPointCut(){
    }

    /**
     * 前置操作 用于拦截操作.
     * @param joinPoint 切点.
     */
//    @Before("logPointCut()")
//    public void doBefore(JoinPoint joinPoint){
//        handleLog(joinPoint, null);
//        System.out.println("前置操作=============");
//    }

    /**
     * 方法返回时操作.
     * @param joinPoint
     * @param ret
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret){
        System.out.println("方法返回时操作=============");
        System.out.println(ret);
        handleLog(joinPoint, ret);
    }

    /**
     * 拦截异常操作.
     * @param joinPoint 切点.
     * @param object 异常.
     */
    protected void handleLog(final JoinPoint joinPoint,final Object object){

        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();



        //从session获取当前用户id.
        String userId = getUserIdByToken();

        String clientIp = request.getRemoteAddr();
        String operateUrl = clientIp//客户端地址
                + ":"
                + request.getServerPort()//端口号
                + request.getContextPath()//项目名称
                + request.getServletPath();//请求页面或其他地址

        try{
            //获取当前用户信息
            AuthUserInfoEntity currentUser = authUserInfoEntityMapper.selectUserByUserId(userId);

            //数据库日志记录
            OperateLog operateLog = new OperateLog();
            operateLog.setId(UUIDUtil.absNumUUID());
            operateLog.setStatus(BusinessStatus.SUCCESS);
            operateLog.setOperateIp(clientIp);//请求的IP
            operateLog.setOperateUserId(userId);//添加操作用户信息.

            //#TODO 临时增加判断用户id是否为空
            if(userId != null){
                //判断当前用户部门是否为空
                if (StringUtils.isNotEmpty(currentUser.getDepartmentId())){
                    operateLog.setDeptName(currentUser.getDepartmentId());//用户部门
                }
            }

            //判断返回结果是否为空
//            if (object != null){
//                ApiResult result = (ApiResult) object;
//                String code = result.getCode();
//                if (code != "1"){
//                    operateLog.setStatus(BusinessStatus.FAIL);
//                    operateLog.setErrorMsg(result.getMessage());
//                }
//            }

            //设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            operateLog.setMethod(className + "." + methodName + "()");
            //设置action动作
            operateLog.setAction("-");
            //设置标题
            operateLog.setModuleName("-");

            // 判断方法上是否有次注解
            Log log = getAnnotationLog(joinPoint);
            if(log != null){
                operateLog.setModuleName(log.module());
                operateLog.setAction(log.action());
            }


            //针对类上的注解
//            boolean clzHasAnno = joinPoint.getTarget().getClass().isAnnotationPresent(Log.class);
//            if (clzHasAnno) {
//                // 获取类上的注解
//                Log log = joinPoint.getTarget().getClass().getAnnotation(Log.class);
//                operateLog.setModuleName(log.module());
//                operateLog.setAction(log.action());
//            }

            operateLog.setOperateTime(new Date());//操作时间.
            operateLog.setOperateUrl(operateUrl);
            operateLogService.insertOperateLog(operateLog);//保存数据库
        } catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("日志记录异常");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }



    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log log.
     * @param operateLog 操作日志对象.
     */
    public void getControllerMethodDescription(Log log, OperateLog operateLog){
        //设置action动作
        operateLog.setAction(log.action());
        //设置标题
        operateLog.setModuleName(log.module());
    }

    /**
     * 针对 方法上自定义注解可用.
     * 判断是否存在注解，如果存在就获取.
     * @param joinPoint 切入点.
     * @return Log对象.
     * @throws Exception 异常信息.
     */
    private Log getAnnotationLog(JoinPoint joinPoint){

        //获取方法的相关信息.
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null){
            return method.getAnnotation(Log.class);
        }
        return null;
    }

}
