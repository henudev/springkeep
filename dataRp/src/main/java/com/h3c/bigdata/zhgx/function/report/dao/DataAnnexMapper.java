package com.h3c.bigdata.zhgx.function.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.h3c.bigdata.zhgx.function.report.entity.DataAnnexEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateItemBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataAnnexMapper extends BaseMapper{
    /**
     * 插入一条数据
     * @param itemBean
     * @return
     */
    int insert(@Param("itemBean") DataAnnexEntity itemBean);

    /**
     * 删除一条数据
     * @param id
     * @return
     */
    int deleteById(@Param("id") String id);

    /**
     * 删除一组数据
     * @param groupId
     * @return
     */
    int deleteByGroupId(@Param("groupId") String groupId);

    /**
     * 查询一条数据
     * @param id
     * @return
     */
    DataAnnexEntity selectById(@Param("id") String id);


    List<String> selectIdsByGroupId(@Param("groupId") String groupId);

    /**
     * 获取附件列表
     * @param groupId
     * @return
     */
    List<DataAnnexEntity> selectAnnexListByGroupId(@Param("groupId") String groupId);

    Map test(@Param("table") String table, @Param("keys") List keys);

    Map getOneDataById(@Param("table") String table, @Param("dataId") String dataId);

    int updateOneData(@Param("table") String table, @Param("dataId") String dataId, String key, String value);

    List<DataAnnexEntity> getAnnexListByIdList(@Param("list") List<String> list);

    List<String> getAnnexFileIdList(@Param("table") String table, @Param("item") String item);

    Map getExtraInfoByAnnexId(@Param("table") String table, @Param("annexId") String annexId,@Param("jydate") String jydate,@Param("volume") String volume, @Param("annexWord") String annexWord);
}
