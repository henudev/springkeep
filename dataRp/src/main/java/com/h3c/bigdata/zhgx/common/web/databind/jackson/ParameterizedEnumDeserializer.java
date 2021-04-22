package com.h3c.bigdata.zhgx.common.web.databind.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.h3c.bigdata.zhgx.common.utils.EnumUtil;
import com.h3c.bigdata.zhgx.common.utils.Reflections;
import com.h3c.bigdata.zhgx.common.web.databind.ParameterizedEnum;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * {@code ParameterizedEnumDeserializer} 参数化枚举类反序列化器
 * <pre>
 *     USAGE: ...
 * </pre>
 *
 * @author f18467
 * @version x.x.x
 * <p>
 * @since 2019/4/4
 */
public class ParameterizedEnumDeserializer extends StdDeserializer<ParameterizedEnum> {

    protected ParameterizedEnumDeserializer() {
        super(ParameterizedEnum.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParameterizedEnum deserialize(final JsonParser jsonParser, final DeserializationContext ctx) throws IOException {
        if (jsonParser.currentToken() != JsonToken.VALUE_STRING) {
            throw new RuntimeException("ParameterizedEnum can only received a string value.");
        }
        String val = jsonParser.getText();
        JsonStreamContext jsc = jsonParser.getParsingContext();
        if (jsc.inObject()) {
            String name = jsc.getCurrentName();
            Object value = jsc.getCurrentValue();
            Optional<Field> field = getMappedField(value.getClass(), name);
            if (field.isPresent()) {
                Class<ParameterizedEnum> fc = (Class<ParameterizedEnum>) field.get().getType();
                if (!ParameterizedEnum.class.isAssignableFrom(fc) || !fc.isEnum()) {
                    throw new RuntimeException("unsupported json deserializer for " + fc.getSimpleName());
                }
                return getMappedEnums(fc, val);
            }
        }
        throw new RuntimeException("unsupported json deserializer for deserialize " + val);
    }

    private ParameterizedEnum getMappedEnums(final Class<ParameterizedEnum> fc, final String currentValue) {
        return EnumUtil.of(fc, e -> e.canResolve(currentValue));
    }

    private Optional<Field> getMappedField(Class<?> cl, String fn) {
        return Reflections.itsField(cl, fn);
    }

}
