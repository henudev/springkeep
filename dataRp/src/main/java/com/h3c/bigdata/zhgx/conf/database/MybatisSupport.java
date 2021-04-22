/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.h3c.bigdata.zhgx.conf.database;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static com.h3c.bigdata.zhgx.common.utils.ObjectUtil.ifNotEmptyAndThen;


/**
 * {@code MybatisSupport}
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/22
 */
public interface MybatisSupport extends DatabaseSupport {

    default SqlSessionFactory initSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        DataSource dataSource = dataSource();
        Assert.state(dataSource != null, "数据源不能为空");
        fb.setDataSource(dataSource);
        ifNotEmptyAndThen(configuration(), fb::setConfiguration);
        ifNotEmptyAndThen(cache(), fb::setCache);
        ifNotEmptyAndThen(configLocation(), fb::setConfigLocation);
        ifNotEmptyAndThen(mapperLocations(), fb::setMapperLocations);
        ifNotEmptyAndThen(transactionFactory(), fb::setTransactionFactory);
        ifNotEmptyAndThen(configurationProperties(), fb::setConfigurationProperties);
        ifNotEmptyAndThen(sqlSessionFactoryBuilder(), fb::setSqlSessionFactoryBuilder);
        ifNotEmptyAndThen(environment(), fb::setEnvironment);
        ifNotEmptyAndThen(failFast(), fb::setFailFast);
        ifNotEmptyAndThen(plugins(), fb::setPlugins);
        ifNotEmptyAndThen(typeHandlers(), fb::setTypeHandlers);
        ifNotEmptyAndThen(typeHandlersPackage(), fb::setTypeHandlersPackage);
        ifNotEmptyAndThen(typeAliases(), fb::setTypeAliases);
        ifNotEmptyAndThen(typeAliasesPackage(), fb::setTypeAliasesPackage);
        ifNotEmptyAndThen(typeAliasesSuperType(), fb::setTypeAliasesSuperType);
        ifNotEmptyAndThen(databaseIdProvider(), fb::setDatabaseIdProvider);
        ifNotEmptyAndThen(vfs(), fb::setVfs);
        ifNotEmptyAndThen(objectFactory(), fb::setObjectFactory);
        ifNotEmptyAndThen(objectWrapperFactory(), fb::setObjectWrapperFactory);
        return fb.getObject();
    }

    default Configuration configuration() {
        return null;
    }

    default Cache cache() {
        return null;
    }

    default Resource configLocation() {
        return null;
    }

    default Resource[] mapperLocations() throws IOException {
        return null;
    }

    default TransactionFactory transactionFactory() {
        return null;
    }

    default Properties configurationProperties() {
        return null;
    }

    default SqlSessionFactoryBuilder sqlSessionFactoryBuilder() {
        return null;
    }

    default String environment() {
        return null;
    }

    default boolean failFast() {
        return false;
    }

    default Interceptor[] plugins() {
        return null;
    }

    default TypeHandler<?>[] typeHandlers() {
        return null;
    }

    default String typeHandlersPackage() {
        return null;
    }

    default Class<?>[] typeAliases() {
        return null;
    }

    default String typeAliasesPackage() {
        return null;
    }

    default Class<?> typeAliasesSuperType() {
        return null;
    }

    default DatabaseIdProvider databaseIdProvider() {
        return null;
    }

    default Class<? extends VFS> vfs() {
        return null;
    }

    default ObjectFactory objectFactory() {
        return null;
    }

    default ObjectWrapperFactory objectWrapperFactory() {
        return null;
    }

}
