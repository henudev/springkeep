package com.h3c.bigdata.zhgx.function.report.serviceImpl;


import com.google.gson.Gson;

import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.function.report.dao.TemplateEntityMapper;
import com.h3c.bigdata.zhgx.function.report.model.*;
import com.h3c.bigdata.zhgx.function.report.service.EsDataOperationService;
import com.h3c.bigdata.zhgx.function.report.util.EsClient;
import com.h3c.bigdata.zhgx.function.report.util.EsUtil;
import org.elasticsearch.action.bulk.*;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author q16926
 * @Title: DataSearchServiceImpl
 * @ProjectName dcplum
 * @Description: 数据检索服务
 * @date 2019/1/715:22
 */
@Service
public class EsDataOperationServiceImpl implements EsDataOperationService {
    Logger logger = LoggerFactory.getLogger(EsDataOperationServiceImpl.class);

    @Autowired
    private TemplateManageServiceImpl templateManageService;
    @Autowired
    private TemplateEntityMapper templateEntityMapper;


    @Override
    public ReturnData searchByKeywords(EsQuery esQuery) throws Exception {
        ReturnData result = new ReturnData();
        String templateNum = esQuery.getTemplateNum();
        String templateSourceName = esQuery.getTemplateSourceNameEn();
        //对于_开头的数据源名称，es中须特殊处理
        if (templateSourceName.startsWith("_")){
            templateSourceName = "a"+templateSourceName;
        }
        Integer id = esQuery.getId();
        //根据模板id获取表字段列表
        List<TemplateItemBean > itemList = templateEntityMapper.queryTemplateItemBeanByNumber(id);
        List<String> fieldList = new ArrayList<>();
        for (TemplateItemBean bean : itemList){
            //关键字是是日期类型，字段列表仅添加日期类型的字段；否则，字段列表中去掉日期类型的字段
            if (StringUtil.isDate(esQuery.getKeywords())){
                if (3 == bean.getType()) {
                    fieldList.add(bean.getItemName());
                }
            }else {
                if (3 != bean.getType()) {
                    fieldList.add(bean.getItemName());
                }
            }
        }
        esQuery.setFieldList(fieldList);
        Map<String, Object> searchData = EsUtil.fuzzyQuery(templateNum, templateSourceName, esQuery,true);
        List<Map<String, Object>> search = (List<Map<String, Object>>) searchData.get("dataList");

        if (search != null) {
            long total = (long) searchData.get("total");

            //itemList.add(new TemplateItemBean("update_user", "更新人"));
            itemList.add(new TemplateItemBean("update_time", "更新时间"));
            //根据模板字段顺序重新排序
            List<Map<String, String>> tmpList = new ArrayList<>();

            for (Map<String, Object> data : search) {
                //排序后的新map
                Map<String, String> dataMap = new HashMap<>();
                while (templateNum.equals(data.get("templateNum").toString())) {
                    for (TemplateItemBean item : itemList) {
                        dataMap.put(item.getItemName(),
                                (data.get(item.getItemName()) == null) ? "" : data.get(item.getItemName()).toString());
                    }
                    break;
                }
                //返回数据id
                dataMap.put("id", data.get("id").toString());
                tmpList.add(dataMap);
            }
            result.setTotal(total);
            result.setDataList(tmpList);
            result.setItemList(itemList);
        }else {
            result.setTotal(0);
        }
        return result;
    }



    @Override
    public int insertEsCommitData(EsInsertModel esInsertModel) throws Exception {
        TransportClient client = EsClient.getClient();
        //获取ES插入数据
        int sum = 0;
        //-----insert
        //1、加入数据权限相关的部门ID、表名ID、表名中文、userid
        String departmentId = esInsertModel.getDepartmentId();
        String templateSourceName = esInsertModel.getTemplateSourceName();
        if (templateSourceName.startsWith("_")){
            templateSourceName = "a"+templateSourceName;
        }
        String templateNumber = esInsertModel.getTemplateNumber();
        String templateCnName = esInsertModel.getTemplateCnName();
        List<Map<String, Object>>  dataList = esInsertModel.getDataMapList();
        BulkProcessor bulkProcessor = bulkProcessor(client);
        for (Map<String, Object> map : dataList) {
            Map<String, Object> tmpMap = new HashMap<>();
            String uuid = map.get("id").toString();
            tmpMap.put("departmentId", departmentId);
            tmpMap.put("templateNum", templateNumber);
            tmpMap.put("templateName", templateCnName);
            tmpMap.putAll(map);
            // 添加需要执行的请求
            IndexRequest indexRequest = new IndexRequest(templateNumber, templateSourceName,uuid).source(tmpMap);
            bulkProcessor.add(indexRequest);
            sum += 1;
        }
        // 刷新请求
        bulkProcessor.flush();
        // 关闭执行器
        bulkProcessor.close();
        bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
        //刷新索引（没有这一步不执行）
        client.admin().indices().prepareRefresh().get();
        return sum;
    }



    /**
     * 删除数据.
     */
    @Override
    public  void delete(String index, String type, List<String> idList) {
        TransportClient client = EsClient.getClient();
        BulkProcessor bulkProcessor = bulkProcessor(client);
        try {
            Arrays.stream(idList.toArray()).forEach(id -> {
                bulkProcessor.add(new DeleteRequest(index, type, id.toString()));
            });
            // 刷新请求
            bulkProcessor.flush();
            bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
            // 关闭执行器
            bulkProcessor.close();
            //刷新索引（没有这一步不执行）
            client.admin().indices().prepareRefresh().get();
        } catch (Exception e) {
            logger.error("****ES 删除数据失败****", e);
        }
    }



    /**
     * @Description: 生成批处理的BulkProcessor
     * @param
     * @return
     * @Author: w15112
     * @Date: 2019/9/4 10:20
     */
    public   BulkProcessor bulkProcessor( TransportClient client){

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {

            public void beforeBulk(long executionId, BulkRequest request) { }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                // 执行批量操作之后，异常
                logger.error("执行错误：" + request.toString() + ",失败：" + failure.getMessage());
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) { }
        };
        // 设置执行器，包含执行时执行过程的监听，以及执行属性配置
        // 设置回滚策略，等待时间50，重试次数3.
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, listener).setBulkActions(1000) // 设置批量处理数量的阀值
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))// 设置批量执执行处理请求大小阀值
                .setFlushInterval(TimeValue.timeValueMillis(100))// 设置刷新索引时间间隔
                .setConcurrentRequests(50)// 设置并发处理线程个数
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueMillis(80), 3))
                .build();
        return bulkProcessor;
    }




}
