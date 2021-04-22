package com.h3c.bigdata.zhgx.function.report.service;

import com.h3c.bigdata.zhgx.function.report.entity.TempToDpt;


import java.util.List;

public interface TempToDptService  {

    int deleteTempToDptByDptId(String departmentId);

    int deleteTempToDptByTempId(String templateId);

    int insert(Integer templateId, String departmentId);

    List<TempToDpt> selectList(Integer templateId);

    int selectCountByDptId(String departmentId);
}
