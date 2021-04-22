package com.h3c.bigdata.zhgx.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * enum工具类
 *
 * @author fangzhiheng
 */
public abstract class EnumUtil {

    private static final Map<Class<?>, Object> enums = new HashMap<>(16);

    /**
     * 判断枚举类是否符合条件
     *
     * @param t   枚举类
     * @param sat 条件判断
     * @param <T> 枚举类型
     *
     * @return 如果符合则返回true，否则返回false
     */
    public static <T> boolean is(Class<T> t, Predicate<? super T> sat) {
        T[] es = values(t);
        if (es != null) {
            for (T e : es) {
                if (sat.test(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找某个枚举类的所有枚举值
     *
     * @param t   枚举类
     * @param <T> 枚举类型
     *
     * @return 所有的枚举
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] values(Class<?> t) {
        Class<?> enumType = getEnumType(t);
        T[] ens = (T[]) enums.get(enumType);
        if (ens == null) {
            synchronized (enums) {
                if ((ens = (T[]) enums.get(t)) == null) {
                    ens = (T[]) t.getEnumConstants();
                    enums.put(enumType, ens);
                }
            }
        }
        return ens;
    }

    /**
     * 查找符合条件的枚举
     *
     * @param t   枚举类
     * @param sat 条件判断
     * @param <T> 枚举类型
     *
     * @return 如果符合则返回，否则返回null
     */
    public static <T> T of(Class<T> t, Predicate<? super T> sat) {
        T[] es = values(t);
        if (es != null) {
            for (T e : es) {
                if (sat.test(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    public static Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType = targetType;
        while ((enumType != null) && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        Objects.requireNonNull(enumType,
                               () -> "The target type " + targetType.getName() + " does not refer toEntity an enum");
        return enumType;
    }

}
