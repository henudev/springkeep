package com.h3c.bigdata.zhgx.function.report.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TableFillLogEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableFillLogEntityMapper extends BaseMapper<TableFillLogEntity> {

    /**
    * @Description: 根据表名查询填报记录列表
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/7/6 14:04
    */
    List<TableFillLogEntity> getTableFillLogsByTableName(@Param("templateId") Integer templateId);

    /**
     * 更新数据填报记录填报数量
     * @param templateId
     * @param updateTime
     * @return
     */
    int updateFillLogSum(@Param("templateId") Integer templateId,@Param("updateTime") String updateTime);
    /**
     * 更新数据填报记录填报次数
     * @param templateId
     * @param updateTime
     * @return
     */
    int updateFillLogCount(@Param("templateId") Integer templateId,@Param("updateTime") String updateTime);

    /**
     * 更新数据填报记录表的模板id
     * @param templateId
     * @param tableName
     * @return
     */
    int updateFillLogTemplateId(@Param("templateId") Integer templateId,@Param("tableName") String tableName);


    /**
    * @Description: 查询晚于updateTime的填报记录
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/8/20 15:25
    */
    List<TableFillLogEntity> getLastFillLogsByUpdateTime(@Param("templateId") Integer templateId,
                                                          @Param("updateTime") String updateTime);

    /**
     * 根据模板删除对应的填报记录
     */
    void deleteByTemplateId(@Param("templateId") Integer templateId);
}