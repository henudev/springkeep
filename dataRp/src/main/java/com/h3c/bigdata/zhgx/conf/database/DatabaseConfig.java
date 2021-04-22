/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.conf.database;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * {@code DatabaseConfig}
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/22
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }

}
