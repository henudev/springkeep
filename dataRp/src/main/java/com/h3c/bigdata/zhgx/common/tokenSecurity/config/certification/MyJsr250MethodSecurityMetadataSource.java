package com.h3c.bigdata.zhgx.common.tokenSecurity.config.certification;

import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Jsr250MethodSecurityMetadataSource;
import org.springframework.security.access.annotation.Jsr250SecurityConfig;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义标签的扫描类
 * @author w17193
 */
@Configuration
public class MyJsr250MethodSecurityMetadataSource extends Jsr250MethodSecurityMetadataSource {

    private String defaultRolePrefix = "ROLE_";

    @Override
    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }

    @Override
    protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
        return processAnnotations(clazz.getAnnotations());
    }

    @Override
    protected Collection<ConfigAttribute> findAttributes(Method method,
                                                         Class<?> targetClass) {
        return processAnnotations(AnnotationUtils.getAnnotations(method));
    }

    /**
     * 重写扫描类 定义loginOpen的扫描标签 并指定顺序 DenyAll>PermitAll>LoginOpen>RolesAllowed
     * @param annotations
     * @return
     */
    private List<ConfigAttribute> processAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        List<ConfigAttribute> attributes = new ArrayList<>();

        for (Annotation a : annotations) {
            if (a instanceof DenyAll) {
                attributes.add(MyJsr250SecurityConfig.DENY_ALL_ATTRIBUTE);
                return attributes;
            }
            if (a instanceof PermitAll) {
                attributes.add(MyJsr250SecurityConfig.PERMIT_ALL_ATTRIBUTE);
                return attributes;
            }

            if (a instanceof LoginOpen){
                attributes.add(MyJsr250SecurityConfig.LOGIN_OPEN_ATTRIBUTE);
                return attributes;
            }

            if (a instanceof RolesAllowed) {
                RolesAllowed ra = (RolesAllowed) a;

                for (String allowed : ra.value()) {
                    String defaultedAllowed = getRoleWithDefaultPrefix(allowed);
                    attributes.add(new Jsr250SecurityConfig(defaultedAllowed));
                }
                return attributes;
            }

        }
        return null;
    }

    private String getRoleWithDefaultPrefix(String role) {
        if (role == null) {
            return role;
        }
        if (defaultRolePrefix == null || defaultRolePrefix.length() == 0) {
            return role;
        }
        if (role.startsWith(defaultRolePrefix)) {
            return role;
        }
        return defaultRolePrefix + role;
    }
}
