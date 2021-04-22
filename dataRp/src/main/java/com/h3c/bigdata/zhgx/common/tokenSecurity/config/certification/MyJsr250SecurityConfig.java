package com.h3c.bigdata.zhgx.common.tokenSecurity.config.certification;

import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import org.springframework.security.access.annotation.Jsr250SecurityConfig;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;

public class MyJsr250SecurityConfig extends Jsr250SecurityConfig {

    public static final Jsr250SecurityConfig PERMIT_ALL_ATTRIBUTE = new Jsr250SecurityConfig(
            PermitAll.class.getName());
    public static final Jsr250SecurityConfig DENY_ALL_ATTRIBUTE = new Jsr250SecurityConfig(
            DenyAll.class.getName());

    public static final Jsr250SecurityConfig LOGIN_OPEN_ATTRIBUTE = new Jsr250SecurityConfig(
            LoginOpen.class.getName());


    public MyJsr250SecurityConfig(String role) {
        super(role);
    }
}
