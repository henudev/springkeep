/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.common.web.databind;

import com.h3c.bigdata.zhgx.common.utils.EnumUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * {@code StringToEnumConverterFactory} 自定义的字符串转枚举
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/20
 */
public class StringToParameterEnumConverterFactory implements ConverterFactory<String, ParameterizedEnum> {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ParameterizedEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Class<T> enumType = (Class<T>) EnumUtil.getEnumType(targetType);
        return new StringToBaseEnumConverter<>(enumType);
    }

    private final class StringToBaseEnumConverter<T extends ParameterizedEnum> implements Converter<String, T> {

        private final Class<T> enumType;

        private final T[] values;

        private final boolean empty;

        private StringToBaseEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
            this.values = EnumUtil.values(enumType);
            this.empty = (values == null) || (values.length == 0);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T convert(String source) {
            return empty ? null : convertInternal(source);
        }

        private T convertInternal(String source) {
            for (T value : values) {
                if (value.canResolve(source)) {
                    return value;
                }
            }
            return null;
        }

        public Class<T> getEnumType() {
            return enumType;
        }
    }

}
