package com.h3c.bigdata.zhgx.common.web.databind.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.h3c.bigdata.zhgx.common.web.databind.ParameterizedEnum;

/**
 * {@code ParameterizedEnumModule}
 * <pre>
 *     USAGE: ...
 * </pre>
 *
 * @author f18467
 * @version x.x.x
 * <p>
 * @since 2019/4/4
 */
public class ParameterizedEnumModule extends SimpleModule {
    public ParameterizedEnumModule() {
        super("ParameterizedEnumModule");
        this.addSerializer(ParameterizedEnum.class, new ParameterizedEnumSerializer());
        this.addDeserializer(ParameterizedEnum.class, new ParameterizedEnumDeserializer());
    }
}
