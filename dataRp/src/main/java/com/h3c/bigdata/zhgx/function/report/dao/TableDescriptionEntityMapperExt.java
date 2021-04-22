package com.h3c.bigdata.zhgx.function.report.dao;


import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName TableDescriptionEntityMapperExt
 * @Description TODO
 * @Author zzzzitai
 * @Date 2018/11/20 9:08
 * @Version 1.0
 **/

public interface TableDescriptionEntityMapperExt {

    @Select({"select * from table_description " +
            " where template_id = #{templateId}"})
    List<TableDescriptionEntity> selectByTemplateId(@Param("templateId") Integer templateId);
    
    @Delete({"delete from table_description " +
            " where template_id = #{templateId}"})
    int deleteByTemplateId(@Param("templateId") Integer templateId);

    @Select({"select count(1) from table_description " +
            " where template_id = #{templateId}" +
            " and type=6"})
    int countAnnex(@Param("templateId") Integer templateId);
    
}
