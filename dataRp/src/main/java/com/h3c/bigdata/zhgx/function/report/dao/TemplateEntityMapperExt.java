package com.h3c.bigdata.zhgx.function.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface TemplateEntityMapperExt extends BaseMapper<TemplateEntity> {



    @Select({"select * from template " +
            " where number = #{number} and template_source_name = #{templateSourceName}"})
    TemplateEntity selectByTemplateId(@Param("templateSourceName") String templateSourceName,
                                      @Param("number") String number);

    @Select({"select * from template " +
            " where name = #{name}"})
    TemplateEntity selectByTemplateName(@Param("name") String name);

    @Insert("insert into template (number, name, description, tag,update_user,update_time," +
            "fill_in_period,template_source_name,is_used)" +
            " values (#{number}, #{name}, #{description}, #{tempTag}, #{updateUser}, #{updateTime}," +
            " #{fillInPeriod},#{templateSourceName},#{isUsed})")
    Integer insertTemplate(@Param("number") String number,
                           @Param("name") String name,
                           @Param("description") String description,
                           @Param("tempTag") String tempTag,
                           @Param("updateUser") String updateUser,
                           @Param("updateTime") Date updateTime,
                           @Param("fillInPeriod") String fillInPeriod,
                           @Param("templateSourceName") String templateSourceName,
                           @Param("isUsed") String isUsed);

    @Select({"select * from template " +
    " where name = #{templateName} and number!=#{number}"})
	TemplateEntity selectByTemplateNameAndId(@Param("templateName") String templateName, @Param("number") String number);

}