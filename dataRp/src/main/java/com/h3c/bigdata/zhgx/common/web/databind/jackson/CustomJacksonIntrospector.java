/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.common.web.databind.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code CustomJacksonIntrospector}
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/20
 */
public class CustomJacksonIntrospector extends JacksonAnnotationIntrospector {

    private static final Pattern SETTER = Pattern.compile("set", Pattern.LITERAL);

    @Override
    protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass) {
        A a = super._findAnnotation(annotated, annoClass);
        if (a == null) {
            AnnotatedElement element = annotated.getAnnotated();
            if (requireProcess(element)) {
                a = process(element, annoClass);
            }
        }
        return a;
    }

    private boolean requireProcess(AnnotatedElement element) {
        return (element instanceof Field) || ((element instanceof Method) && ((Member) element).getName().startsWith("set"));
    }

    private <A extends Annotation> A process(AnnotatedElement element, Class<A> annoClass) {
        if (element instanceof Method) {
            return tryFindAnnotation((Method) element, annoClass);
        } else if (element instanceof Field) {
            return tryFindAnnotation((Field) element, annoClass);
        }
        return null;
    }

    private <A extends Annotation> A tryFindAnnotation(Method method, Class<A> annoClass) {
        if (method == null || annoClass == null) {
            return null;
        }
        A a = AnnotationUtils.findAnnotation(method, annoClass);
        if ((a == null) && method.getName().startsWith("set")) {
            a = tryFindAnnotation(ReflectionUtils.findField(method.getDeclaringClass(), setter2field(method.getName())), annoClass);
        }
        return a;
    }

    private String setter2field(String setter) {
        return Introspector.decapitalize(SETTER.matcher(setter).replaceFirst(Matcher.quoteReplacement("")));
    }

    private <A extends Annotation> A tryFindAnnotation(Field field, Class<A> annoClass) {
        if (field == null || annoClass == null) {
            return null;
        }
        return AnnotationUtils.findAnnotation(field, annoClass);
    }

}
