package com.h3c.bigdata.zhgx.function.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateAddBean;
import com.h3c.bigdata.zhgx.function.report.model.TemplateItemBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TableDescriptionEntityMapperExt
 * @Description TODO
 * @Author zzzzitai
 * @Date 2018/11/20 9:08
 * @Version 1.0
 **/

public interface TableDescriptionEntityMapper extends BaseMapper<TableDescriptionEntity> {

    List<TableDescriptionEntity> selectByTemplateId(@Param("templateId") Integer templateId);

    @Delete({"delete from table_description " +
            " where template_id = #{templateId}"})
    int deleteByTemplateId(@Param("templateId") Integer templateId);

    @Insert("insert into table_description (number,item, name,type,is_null," +
            "enums,is_union_only,is_search,template_id,is_sort)" +
            "values (#{number},#{item},#{name},#{type},#{isNull},#{enums},#{isUnionOnly}," +
            "#{isSearch},#{templateId},#{isSort})")
    Integer insertTableDescription(@Param("number") String number,
                                   @Param("item") String item,
                                   @Param("name") String name,
                                   @Param("type") int type,
                                   @Param("isNull") Byte isNull,
                                   @Param("enums") String enums,
                                   @Param("isUnionOnly") Byte isUnionOnly,
                                   @Param("isSearch") Byte isSearch,
                                   @Param("templateId") Integer templateId,
                                   @Param("isSort") Byte isSort);
    Integer insertTableDescriptionEntity(@Param("itemBeans") List<TemplateItemBean> itemBeans);
	void updateDescByTmpNu(TemplateAddBean template);

	void updateDescCommentByTmpNu(List<String> updateDescCommentSql);

	List<String> selectUnionOnly(Integer templateId);

	List<String> selectAnnex(Integer templateId);

	String selectAnnexIds(@Param("number") String number, @Param("annex") String annex, @Param("dataId") String dataId);

	List<String> selectUnionOnlyData(@Param("unionOnlyKey") List<String> unionOnlyKey, @Param("number") String number);

	void deleteByUnionOnlyKeys(@Param("unionOnlyKeys") List<Map<String, String>> unionOnlyKeys, @Param("number") String number);

	int execSql(@Param("sql") String sql);

	int selectByUnionOnlyKeys(@Param("unionOnlyKeys") Map<String, String> unionOnlyKeys,@Param("id") String id, @Param("number") String number);

    /**
     * selectIdsByUnionOnlyKeys
     * @param unionOnlyKeys
     * @param keys
     * @param number
     * @return
     */
    List<String> selectIdsByUnionOnlyKeys(@Param("unionOnlyKeys") List<Map<String, String>> unionOnlyKeys, @Param("keys") List<String> keys, @Param("number") String number);

	int updateData(@Param("data") Map<String, Object> data,@Param("id") String id, @Param("number") String number);

	void deleteBatchByUnionOnlyKeys(@Param("unionOnlyKeys") List<Map<String, String>> unionOnlyKeys, @Param("keys") List<String> keys, @Param("number") String number);
    /**根据表明和字段名，获取字段对应的注释*/
    @Select({"select * from table_description " +
            " where number = #{tableName} and item =#{item}"})
    TableDescriptionEntity getTableByTableNameAndItem(@Param("tableName") String tableName,@Param("item") String item);
    int delData(@Param("number") String number,@Param("id") String id);
    int delDataByTime(@Param("number") String number,@Param("updateTime") String updateTime);


    List<Map<String,Object>> queryData(@Param("number") String number,@Param("conditions") Map<String,Object> conditions,int page,int size);

    int queryDataCount(@Param("number") String number,@Param("conditions") Map<String,Object> conditions);

    /**
     * 根据表名和字段名查询已存在的字段名
     */
    String getTableItem(@Param("templateSourceName") String templateSourceName,
                        @Param("tableName") String tableName, @Param("item") String item);

    /**
     * 更新填报字段表的模板id
     *
     * @param templateId
     * @param tableName
     * @return
     */
    int updateDescriptionTemplateId(@Param("templateId") Integer templateId, @Param("tableName") String tableName);
}
