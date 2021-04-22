package com.h3c.bigdata.zhgx.function.report.service;

import com.baomidou.mybatisplus.service.IService;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import com.h3c.bigdata.zhgx.function.report.model.DelData;
import com.h3c.bigdata.zhgx.function.report.model.QueryData;
import com.h3c.bigdata.zhgx.function.report.model.ReportData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DataProcessService
 * @Description 数据
 * @Author zzzzitai
 * @Date 2018/11/14 12:11
 * @Version 1.0
 **/
public interface DataProcessService extends IService<TemplateEntity> {
    /**
     * 上传excel读取数据
     * @param file
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> uploadFile(MultipartFile file,Integer templateId) throws Exception;

    /**
     * 校验用户填报内容
     * @throws Exception exception
     * @return 
     */
    Map<String, Object> saveData(ReportData reportData, String userId)throws Exception;
    /**
     * 删除信息.
     * @param delData 待删除数据实体
     * @throws Exception exception
     */
    int deleteData(DelData delData, String userId) throws Exception;

    /**
     * 清空此模板.
     * @param templateId 模板ID
     * @throws Exception exception
     */
    int deleteAllDataByTemplateId(Integer templateId) throws Exception;

    /**
     * 更新信息.
     * @param data 待更新的数据
     * @throws Exception exception
     */
     Object updateData(ReportData data, String userId) throws Exception;


    /**
     * 查询数据信息
     * @param queryData
     * @return
     */
    Map<String,Object> queryData(QueryData queryData);


    /**
     * @param
     * @return
     * @Description: 删除填报记录
     * @Author: w15112
     * @Date: 2019/8/20 14:27
     */
    void deleteFillLogByUpdateTime(Integer templateId, String updateTime, String userId);
    /**
     * 将现场表数据添加至es中,false时处理更新小数、整数字段数据
     * @param pageNum
     */
    void InsertOldDataToEs(int pageNum) throws Exception ;

    /**
     * 将现场单条数据添加至es中
     */
    void InsertSingleDataToEs(Integer templateId) throws Exception;

    /**
     * 批量更新整数、小数字段数据
     */
    void editOldDataToEs();

    /**
     * 数据替换策略.
     * @param map
     * @param
     * @return
     * @throws Exception
     */
    Object strategyData(Map<String, String> map)throws Exception;
}
