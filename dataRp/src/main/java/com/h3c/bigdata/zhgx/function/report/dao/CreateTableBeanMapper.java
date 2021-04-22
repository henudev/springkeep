package com.h3c.bigdata.zhgx.function.report.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

public interface CreateTableBeanMapper {

    @Options(statementType = StatementType.CALLABLE)
    @Update({"create table ${name} ${fields}" +
            " ENGINE=InnoDB DEFAULT CHARSET=utf8"})
    int createTable(@Param("name") String name, @Param("fields") String fields);

    @Delete({"drop table if exists ${name}"})
    int deleteTable(@Param("name") String name);

    @Select({"SELECT COUNT(*) FROM ${tableName}"})
    int selectDataCount(@Param("tableName") String tableName);

    @Insert({"CREATE TABLE ${newTableName} AS SELECT * FROM ${tableName}"})
    int bakTableData(@Param("newTableName") String newTableName, @Param("tableName") String tableName);

    @Select({"select count(1) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='${sourceName}' AND " +
            "TABLE_NAME= '${tableName}'"})
    int checkTableIfExist(@Param("sourceName") String sourceName,@Param("tableName") String tableName);

    @Delete({"delete from  ${name}"})
    int deleteData(@Param("name") String name);
}
