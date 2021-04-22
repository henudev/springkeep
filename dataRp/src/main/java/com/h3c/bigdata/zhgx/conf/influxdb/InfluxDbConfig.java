package com.h3c.bigdata.zhgx.conf.influxdb;

import org.influxdb.InfluxDB;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 源自rh-mlmh
 * @author yys1402
 * @version 1.0.0
 * @since D001
 */
@Configuration
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxDbConfig {

    private String database;

    @Bean
    @ConditionalOnProperty("spring.influx.database")
    public InfluxDbClient influxDbClient(InfluxDB influxDB){

        return new InfluxDbClient(database, influxDB);
    }

    public void setDatabase(final String database) {
        this.database = database;
    }
}
