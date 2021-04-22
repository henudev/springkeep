/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.common.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@code ObjectUtil} 是处理Object的一系列工具类集合
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/2/25
 */
public final class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 判断传入参数是否全为空
     *
     * @param objects the objects
     *
     * @return the boolean
     */
    @Contract(pure = true)
    public static boolean isAllNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

    @Contract(pure = true)
    public static boolean isNoneNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

    @Contract(value = "_ -> param1", pure = true)
    @SafeVarargs
    @NotNull
    public static <E> E[] toArray(E... es) {
        return es;
    }

    @Nullable
    public static <T> T ifHasNull(@NotNull Object[] objects, @NotNull Supplier<? extends T> supplier) {
        return ifHasNull(objects, supplier, null);
    }

    @Nullable
    public static <T> T ifHasNull(@NotNull Object[] objects, @NotNull Supplier<? extends T> supplier,
                                  @Nullable T defaultVal) {
        return !isNoneNull(objects) ? supplier.get() : defaultVal;
    }

    @Nullable
    public static <P, T> T ifSatisfied(@NotNull P[] objects, @NotNull Predicate<? super P> condition,
                                       @NotNull Supplier<? extends T> supplier) {
        return ifSatisfied(objects, condition, supplier, null);
    }

    @Nullable
    public static <P, T> T ifSatisfied(@NotNull P[] objects, @NotNull Predicate<? super P> condition,
                                       @NotNull Supplier<? extends T> supplier, @Nullable T defaultVal) {
        for (P object : objects) {
            if (condition.test(object)) {
                return supplier.get();
            }
        }
        return defaultVal;
    }

    public static <P> void ifNotEmptyAndThen(P p, Consumer<P> consumer) {
        if ((p == null) || (p.getClass().isArray() && (Array.getLength(
                p) == 0)) || ((p instanceof Collection) && !((Collection) p).isEmpty()) || ((p instanceof String) && StringUtil
                .isNotBlank((String) p))) {
            return;
        }
        consumer.accept(p);
    }

    public static <T> T nullOrDefault(T a, T b) {
        return a == null ? b : a;
    }

    public static <P, R> R ifNotNull(P p, Function<P, R> getter, R defaultValue) {
        return p == null ? defaultValue : getter.apply(p);
    }

}
