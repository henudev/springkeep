package com.h3c.bigdata.zhgx.common.web.databind.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.h3c.bigdata.zhgx.common.web.databind.ParameterizedEnum;

import java.io.IOException;

/**
 * {@code ParameterizedEnumSerializer} 参数化枚举类序列化器
 * <pre>
 *     USAGE: ...
 * </pre>
 *
 * @author f18467
 * @version x.x.x
 * <p>
 * @since 2019/4/4
 */
public class ParameterizedEnumSerializer extends JsonSerializer<ParameterizedEnum> {

    @Override
    public void serialize(final ParameterizedEnum value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeString(value.getRequestParameter());
    }

}
