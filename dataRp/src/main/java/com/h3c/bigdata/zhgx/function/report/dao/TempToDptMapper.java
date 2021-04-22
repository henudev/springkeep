package com.h3c.bigdata.zhgx.function.report.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TempToDpt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempToDptMapper extends BaseMapper<TempToDpt> {


    /**
    * @Description: 根据模板id获取对应的部门列表
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/6/18 16:20
    */
    List<String> getDptNameByTemplateId(@Param("id") Integer id);
}
