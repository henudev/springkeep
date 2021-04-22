package com.h3c.bigdata.zhgx.function.report.service;


import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.function.report.model.EsInsertModel;
import com.h3c.bigdata.zhgx.function.report.model.EsQuery;
import com.h3c.bigdata.zhgx.function.report.model.ReturnData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author q16926
 * @Title: DataSearchService
 * @ProjectName dcplum
 * @Description: 数据检索
 * @date 2019/1/715:21
 */
public interface EsDataOperationService {
    /**
     　　* @Description: TODO
     　　* @param
     　　* @return
     　　* @throws
     　　* @author q16926
     　　* @date 2018/12/24 11:21
     　　*/
    ReturnData searchByKeywords(EsQuery esQuery)throws Exception;

    /**
    　　* @Description: 提交数据插入ES
    　　* @param
    　　* @return int
    　　* @throws
    　　* @date 2019/1/9 14:32
    　　*/
    int insertEsCommitData(EsInsertModel esInsertModel) throws Exception;



    /**
     * 删除es数据
     */
     void delete(String index, String type, List<String> idList);


}
