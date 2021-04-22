package com.h3c.bigdata.zhgx.function.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateSourceEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TemplateSourceEntityMapper extends BaseMapper<TemplateSourceEntity> {

    @Select({"SELECT COUNT(*) FROM information_schema.schemata " +
            " WHERE schema_name= #{dataBaseName}"})
    int countDataBase(@Param("dataBaseName") String dataBaseName);

    @Select({"select count(*) from information_schema.TABLES t " +
            " WHERE  t.TABLE_SCHEMA = #{dataBaseName}"})
    int coutTablesByDataBaseName(@Param("dataBaseName") String dataBaseName);


    List<TemplateSourceEntity> selectTemplateByCNname(@Param("template") TemplateSourceEntity templateSourceEntity);

    @Select({"select * from template_source " +
            " where id = #{sourceId}"})
    TemplateSourceEntity selectTemplateById(@Param("sourceId") String sourceId);

    @Delete({"delete from template_source " +
            " where id = #{sourceId}"})
    int deleteSourceById(@Param("sourceId") String sourceId);

    List<TemplateSourceEntity> selectTemplateList(@Param("CNname") String CNname, @Param("ENname") String ENname);

    int updateTemplateSource(TemplateSourceEntity templateSourceEntity);

    int insertTemplateSource(TemplateSourceEntity templateSourceEntity);
}
