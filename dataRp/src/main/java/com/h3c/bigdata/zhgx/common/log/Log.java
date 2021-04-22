package com.h3c.bigdata.zhgx.common.log;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解.
 *
 * @Author J16898
 * @Date 2018/7/31
 * @Version 1.0
 */
@Target({ElementType.METHOD})//注解的作用目标:方法和类
@Retention(RetentionPolicy.RUNTIME)//注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Documented//说明该注解将会保存在javadoc中
public @interface Log {

    /** 模块 */
    String module() default "";

    /** 功能 */
    String action() default "";

    /** 是否保存请求的参数 */
    boolean isSaveRequestData() default true;
}
