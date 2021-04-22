package com.h3c.bigdata.zhgx.function.report.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateSourceEntity;

/**
 * 模板源管理
 * @date 2019-10-11 17:07:54
 * @author y19714
 */
public interface TemplateSourceService{

    /**
     * 根据数据库名查询该数据库是否已存在
     * @param dataBaseName
     * @return
     */
    int countDataBase(String dataBaseName);

    /**
     * 创建数据库
     */
    int createDataBase(String dataBaseName);

    /**
     * 新增数据源
     * @param templateSourceEntity
     * @return
     */
    ApiResult insertTemplateSource(TemplateSourceEntity templateSourceEntity);

    ApiResult queryTemplateSourceList(int pageNum, int pageSize,String orderBy,String dir,TemplateSourceEntity templateSourceEntity);

    ApiResult updateTemplateSource(TemplateSourceEntity templateSourceEntity);

    ApiResult countTemplateSource(TemplateSourceEntity templateSourceEntity);

    ApiResult deleteTemplateSouece(String sourceId);
}
