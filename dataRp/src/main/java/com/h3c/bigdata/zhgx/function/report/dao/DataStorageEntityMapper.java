package com.h3c.bigdata.zhgx.function.report.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataStorageEntityMapper{
    int insert(@Param("storageId")String storageId);
    int delete(@Param("storageId")String storageId);
    List<String> query();
}