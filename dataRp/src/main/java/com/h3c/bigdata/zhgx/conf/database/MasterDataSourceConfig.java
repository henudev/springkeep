package com.h3c.bigdata.zhgx.conf.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @program: openPortal_service
 * @description: 主数据源配置
 * @author: h17338
 * @create: 2018-11-06 15:28
 **/
@Configuration
@AutoConfigureAfter(DatabaseConfig.class)
@MapperScan(basePackages = MasterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig implements MybatisSupport {

    // dao路径
    static final String PACKAGE = "com.h3c.bigdata.zhgx.function.*.dao";

    //mapper路径
    static final String MAPPER_LOCATION = "classpath*:mapper/master/**/*.xml";

    private final org.apache.ibatis.session.Configuration configuration;

    @Autowired
    public MasterDataSourceConfig(org.apache.ibatis.session.Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public org.apache.ibatis.session.Configuration configuration() {
        return configuration;
    }

    @Override
    public Resource[] mapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION);
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    @Override
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return initSqlSessionFactory();
    }

}
