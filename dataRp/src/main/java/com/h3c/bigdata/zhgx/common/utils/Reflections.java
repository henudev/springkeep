package com.h3c.bigdata.zhgx.common.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author fangzhiheng
 */
public abstract class Reflections {

    private static final Map<Class<?>, Field[]> CLASS_FIELD_MAPPING = new HashMap<>(16);

    public static Method setter(@NotNull Object o, Field f, Class<?> type) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(setter(f.getName()), type);
    }

    @NotNull
    public static String setter(@NotNull String f) {
        return "set" + StringUtil.upperSpec(f, 0);
    }

    public static Method getter(@NotNull Object o, Field f) throws NoSuchMethodException {
        return o.getClass().getDeclaredMethod(getter(f.getName()));
    }

    @NotNull
    public static String getter(@NotNull String f) {
        return "get" + StringUtil.upperSpec(f, 0);
    }

    public static Optional<Object> invoke(@NotNull Class<?> cl, Object o, String methodName, Object[] parameters,
                                          Class[] parameterTypes) {
        try {
            Method method = cl.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(o, parameters));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Optional<T> construct(@NotNull Class<T> c, Object[] parameters, Class[] parameterTypes) {
        try {
            Constructor<T> constructor = c.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return Optional.of(constructor.newInstance(parameters));
        } catch (NoSuchMethodException |
                         IllegalAccessException |
                         InstantiationException |
                         InvocationTargetException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<Field> itsField(@NotNull Class<T> t, String fieldName) {
        try {
            return Optional.ofNullable(t.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Type[] fieldGenericTypes(@NotNull Field field) {
        Type t = field.getGenericType();
        if (t instanceof ParameterizedType) {
            ParameterizedType tt = (ParameterizedType) t;
            return tt.getActualTypeArguments();
        }
        return new Type[0];
    }

    public static <T> T copyInit(@NotNull Object source, @NotNull Class<T> target, int modifier) {
        T t;
        try {
            t = target.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copy(source, t, modifier);
        return t;
    }

    public static void copy(@NotNull Object source, @NotNull Object target, int modifier) {
        Class<?> sClass = source.getClass();
        Class<?> tClass = target.getClass();
        Field[] sFields = findFiledsOrInhreited(sClass);

        Map<String, Field> requireCopied = new HashMap<>(sFields.length * 2);

        for (Field field : sFields) {
            if ((field.getModifiers() & modifier) == field.getModifiers()) {
                field.setAccessible(true);
                requireCopied.put(field.getName(), field);
            }
        }

        Field[] tFields = findFiledsOrInhreited(tClass);

        for (Field field : tFields) {
            String fieldName = field.getName();

            if (!requireCopied.containsKey(fieldName)) {
                continue;
            }

            Object requireCopiedValue = null;

            try {
                Method sGetter = sClass.getDeclaredMethod(getter(fieldName));
                sGetter.setAccessible(true);
                requireCopiedValue = sGetter.invoke(source);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                try {
                    requireCopiedValue = requireCopied.get(fieldName).get(source);
                } catch (IllegalAccessException ignored) {
                }
            }

            try {
                Method tSetter = tClass.getDeclaredMethod(setter(fieldName));
                tSetter.setAccessible(true);
                tSetter.invoke(target, requireCopiedValue);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                try {
                    field.setAccessible(true);
                    field.set(target, requireCopiedValue);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    public static Field[] findFiledsOrInhreited(Class<?> clazz) {
        if (CLASS_FIELD_MAPPING.containsKey(clazz)) {
            return CLASS_FIELD_MAPPING.get(clazz);
        }
        if (clazz == Object.class) {
            return new Field[0];
        }
        Field[] result;
        synchronized (CLASS_FIELD_MAPPING) {
            List<Field> fields = new ArrayList<>(16);
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            Field[] inhreitedFields = findFiledsOrInhreited(clazz.getSuperclass());
            if (inhreitedFields.length != 0) {
                fields.addAll(Arrays.asList(inhreitedFields));
            }
            result = fields.toArray(new Field[0]);

            CLASS_FIELD_MAPPING.put(clazz, result);
        }
        return result;
    }

}
