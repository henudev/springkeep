package com.h3c.bigdata.zhgx.common.tokenSecurity.annotation;

import java.lang.annotation.*;

/**
 * 登录后即可访问的标签
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoginOpen {
}