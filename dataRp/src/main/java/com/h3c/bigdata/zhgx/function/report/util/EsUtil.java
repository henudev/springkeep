package com.h3c.bigdata.zhgx.function.report.util;

import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.function.report.model.EsInsertModel;
import com.h3c.bigdata.zhgx.function.report.model.EsQuery;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;

import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author q16926
 * @Title: EsClientUtil
 * @ProjectName dcplum
 * @Description: ES连接器
 * @date 2019/1/813:59
 */
@Component
public class EsUtil {
    static Logger log = LoggerFactory.getLogger(EsUtil.class);
    static TransportClient client = EsClient.getClient();

    /**
     * 存储.
     */
    public static void insert(String esIndex, String esType, String id, Map<String, String> map) {
        
        IndexResponse indexResponse = client.prepareIndex(esIndex, esType, map.get("id"))
                .setSource(map)
                .get();
    }

    /**
     * 删除字段.
     */
    public static void deleteField(String esIndex, String esType, String field) {
       
        try {
            DeleteResponse deleteResponse = client.prepareDelete(esIndex, esType, field).get();
            log.info("ES 删除字段: " + field);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ES 删除字段: " + field + "  失败");
        }
    }

    /**
     * 删除数据.
     */
    public static void delete(String index, String type, List<String> idList) {
       
        try {
            Arrays.stream(idList.toArray()).forEach(id -> {
                DeleteRequestBuilder drb = client.prepareDelete(index, type, id.toString()).
                        setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                DeleteResponse response = drb.execute().actionGet();
                log.info("ES 删除数据 id : " + id);
            });
        } catch (Exception e) {
            log.error("****ES 删除数据失败****", e);
        }
    }

    /**
     * 根据数据ID删除数据.
     */
    public static void deleteByTmpNum(String index, String templateNum) {
      
        try {
            DeleteIndexResponse deleteIndexResponse = client.admin().indices()
                    .prepareDelete(index)
                    .execute().actionGet();
            log.info("****ES删除模板：****" + templateNum + "*** 成功：");
        } catch (Exception e) {
            log.error("****ES删除模板失败****", e);
        }
    }

//    /**
//     * 删除数据.
//     */
//    public static   void delete(String index, String type, List<String> idList) {
//        BulkProcessor bulkProcessor = bulkProcessor(client);
//        try {
//            Arrays.stream(idList.toArray()).forEach(id -> {
//                bulkProcessor.add(new DeleteRequest(index, type, id.toString()));
//            });
//            // 刷新请求
//            bulkProcessor.flush();
//            bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
//            // 关闭执行器
//            bulkProcessor.close();
//            //刷新索引（没有这一步不执行）
//            client.admin().indices().prepareRefresh().get();
//        } catch (Exception e) {
//            log.error("****ES 删除数据失败****", e);
//        }
//    }

    /**
     * @Description: 生成批处理的BulkProcessor
     * @param
     * @return
     * @Author: w15112
     * @Date: 2019/9/4 10:20
     */
    public static BulkProcessor bulkProcessor(TransportClient client){

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long executionId, BulkRequest request) { }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                // 执行批量操作之后，异常
                log.error("执行错误：" + request.toString() + ",失败：" + failure.getMessage());
            }

            @Override
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


    public static int insertEsCommitData(EsInsertModel esInsertModel) throws Exception {
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
     * 查询 type=false:数据集预览查询，type=true数据填报查询
     */
    public static Map<String, Object> fuzzyQuery(String esIndex, String esType,
                                                 EsQuery esQuery, Boolean type) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> dataList = new LinkedList<>();

        int page = esQuery.getPage();
        int pageSize = esQuery.getPageSize();
        String updateTime = esQuery.getUpdateTime();
        String keywords = StringUtils.deleteWhitespace( esQuery.getKeywords());
        String keyword =  "*"+keywords+"*";
        String templateNum = esQuery.getTemplateNum();
        String startTime = esQuery.getStartTime();
        String endTime = esQuery.getEndTime();
        String orderField =esQuery.getOrderField();
        String dir = esQuery.getDir();
        SortOrder sortOrder = "desc".equals(dir)? SortOrder.DESC:SortOrder.ASC;
        SortBuilder sortBuilder = SortBuilders.fieldSort(orderField)
                .order(sortOrder).unmappedType("long");
        List<String> fieldList = esQuery.getFieldList();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (indexExists(esIndex)) {
            if (type) {
                if (StringUtils.isNotBlank(updateTime)) {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("templateNum", templateNum))
                            .must(QueryBuilders.matchPhraseQuery("update_time", updateTime));
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("templateNum", templateNum));
                    if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("update_time").from(startTime).to(endTime));
                    }
                }
            }

            SearchRequestBuilder builder  = null;
            if (type){
                //对检索字段遍历匹配,keyword为空获取全部，不为空返回检索到的记录
                BoolQueryBuilder fieldQueryBuilder = QueryBuilders.boolQuery();
                if (StringUtils.isNotBlank(esQuery.getKeywords())){
                    for (String str : fieldList) {
                        if (StringUtil.isOnlyLetterOrDigit(keywords)) {
                            fieldQueryBuilder.should(QueryBuilders.wildcardQuery(str, keyword.toLowerCase()));
                        } else if (StringUtil.isDate(keywords)) {
                            boolQueryBuilder.must(QueryBuilders.rangeQuery(str).from(keywords).to(keywords));
                        } else {
                            fieldQueryBuilder.should(QueryBuilders.matchPhraseQuery(str, keyword));
                        }
                    }
                    boolQueryBuilder.must(fieldQueryBuilder);
                }else {
                    //获取所有的数据
                    boolQueryBuilder.must(QueryBuilders.queryStringQuery(keyword));
                }
                 builder = client.prepareSearch(esIndex).setTypes(esType).
                        setQuery(boolQueryBuilder).addSort(sortBuilder);
            }else {
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keyword));
                 builder = client.prepareSearch(esIndex).setTypes(esType).
                        setQuery(boolQueryBuilder);
            }
            builder = builder.setFrom((page - 1) * pageSize).setSize(pageSize);
            SearchResponse searchResponse = builder.get();
            SearchHits hits = searchResponse.getHits();
            SearchHit[] hits1 = hits.getHits();
            Arrays.stream(hits1).forEach(hit -> {
                dataList.add(hit.getSource());
            });
            result.put("dataList", dataList);
            result.put("total", hits.getTotalHits());
        }
        return result;
    }

    /**
     * 检查索引是否存在
     */
    public static boolean indexExists(String index) {
        IndicesAdminClient adminClient = client.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
    }

    /**
     * 搜索Title并且高亮显示
     */
    public static Map<String, Object> searchTitle(EsQuery esQuery, String esIndex, String[] fieldList) {
      
        Map<String, Object> result = new HashMap<>();
        List<String> fieldLists = Arrays.asList(fieldList);
        // 统计查询时间,这里开始
        Instant start = Instant.now();
        // 构造查询条件,使用标准分词器.
        int page = esQuery.getPage();
        int pageSize = esQuery.getPageSize();
        String orderField = esQuery.getOrderField();
        String dir = esQuery.getDir();
        SortOrder sortOrder = "desc".equals(dir)? SortOrder.DESC:SortOrder.ASC;

        String keyword =esQuery.getKeywords();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
         boolQueryBuilder = getBoolQueryBuilder(boolQueryBuilder,esQuery);
        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        BoolQueryBuilder fieldQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(esQuery.getKeywords())){
            setHighField(fieldLists, keyword, boolQueryBuilder, highlightBuilder, fieldQueryBuilder);
        }else {
            //获取所有的数据目录
            boolQueryBuilder.must(QueryBuilders.queryStringQuery("*"+keyword+"*"));
        }

        highlightBuilder = highlightBuilder.preTags("<span class=\"important\">").postTags("</span>");

        SearchResponse searchResponse = client.prepareSearch(esIndex).setTypes(esIndex).
                setQuery(boolQueryBuilder).addSort(orderField,sortOrder).
                highlighter(highlightBuilder).setFrom((page - 1) * pageSize).setSize(pageSize).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        // 统计搜索结束时间
        Instant end = Instant.now();
        List<Map<Object, Object>> dataList = new ArrayList();
        for ( SearchHit hit : hits1) {
            // 遍历结果,使用HashMap存放
            Map<Object, Object> map = new LinkedHashMap();
            // 返回文档所在的ID编号及目录或者资源标志位
            String type = hit.getSource().get("type").toString();
            map.put("id", hit.getId());
            map.put("type", type);
            map.put("catalogId", hit.getSource().get("catalogId"));
            map.put("title", hit.getSource().get("name"));
            map.put("clickName", hit.getSource().get("parentDepartment"));
            if ("catalog".equals(type)){
                map.put("api", hit.getSource().get("api"));
                map.put("file", hit.getSource().get("file"));
                map.put("db", hit.getSource().get("db"));
                map.put("parentDepartment", hit.getSource().get("parentDepartment"));
                map.put("clickDepartment", hit.getSource().get("department"));
                map.put("categoryId", hit.getSource().get("categoryId"));
            }else if ("app".equals(type)){
                map.put("photo", hit.getSource().get("photo"));
                map.put("score", hit.getSource().get("score"));
            }
            map.put("visitCount", hit.getSource().get("visitCount"));
            // 返回文档的高亮字段
            map = setHighText(fieldLists,map,hit);
            dataList.add(map);
        }
        result.put("dataList", dataList);
        result.put("total",  hits.getTotalHits() );
        result.put("time",Duration.between(start, end).toMillis() );
        return result;
    }

    private static void setHighField(List<String> fieldLists, String keyword, BoolQueryBuilder boolQueryBuilder,
                                     HighlightBuilder highlightBuilder, BoolQueryBuilder fieldQueryBuilder) {

        fieldLists = fieldLists.subList(0,2);
        for (String str : fieldLists) {
            HighlightBuilder.Field highlightField = new HighlightBuilder.Field(str);
            highlightBuilder.field(highlightField);
            if (StringUtil.isOnlyLetterOrDigit(keyword)){
                fieldQueryBuilder.should(QueryBuilders.wildcardQuery(str, "*"+keyword.toLowerCase()+"*"));
            }else {
                fieldQueryBuilder.should(QueryBuilders.matchPhraseQuery(str, "*"+keyword+"*"));
            }
        }
        boolQueryBuilder.must(fieldQueryBuilder);
    }

    @NotNull
    private static Map<Object, Object> getresponseMap(List<String> modelList, List<String> fieldLists, SearchHit hit) {
        // 遍历结果,使用HashMap存放
        Map<Object, Object> map = new LinkedHashMap();
        Map modelMap = new HashMap<>();
        // 返回文档的高亮字段
        map = setHighText(fieldLists,map,hit);
        modelMap = setHighText(modelList,modelMap,hit);
        map.put("regcap", hit.getSource().get("regcap"));
        map.put("pripid",hit.getSource().get("pripid"));
        map.put("model",modelMap);
        return map;
    }


    //设置字体高亮
    public static  Map<Object, Object> setHighText(List<String> fieldLists,Map<Object, Object> map,SearchHit hit){
        for (String highText : fieldLists){
            if (hit.getHighlightFields().get(highText) != null){
                String hight = "";
                Text[] text = hit.getHighlightFields().get(highText).getFragments();
                if (text != null) {
                    for (Text str : text) {
                        hight += str;
                        log.info(str.toString());
                    }
                }
                map.put(highText, hight);
            }else{
                map.put(highText,hit.getSource().get(highText));
            }
        }
        return  map;

    }


    /**
    * @Description: 设置目录过滤条件
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/9/28 14:02
    */
    @NotNull
    private static BoolQueryBuilder getBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder, EsQuery esQuery) {

        String department = esQuery.getDepartment();
        String theme = esQuery.getTheme();
        String base = esQuery.getBase();
        String resourceType = esQuery.getResourceType();
        String type = esQuery.getType();
        String catalogDepartmentLevel = esQuery.getCatalogDepartmentLevel();
        String categoryId = esQuery.getCategoryId();
        //构造过滤条件
        if (!"all".equals(type)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("type", type));
        }
        //目录分主题/基础/部门筛选
        if ("catalog".equals(type)) {
            if ("部门".equals(department)) {
                boolQueryBuilder.must(QueryBuilders.existsQuery("department"));
            } else if (StringUtils.isNotBlank(department)) {
                if (StringUtils.isNotBlank(categoryId)){
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("categoryId", categoryId));
                }
                if ("1".equals(catalogDepartmentLevel)){
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("parentDepartment", department));
                }else {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("department", department));
                }

            }
            if ("主题".equals(theme)) {
                boolQueryBuilder.must(QueryBuilders.existsQuery("theme")).
                        must(QueryBuilders.existsQuery("theme"));
            } else if (StringUtils.isNotBlank(theme)) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("theme", theme));
            }
            if ("基础".equals(base)) {
                boolQueryBuilder.must(QueryBuilders.existsQuery("base"))
                        .mustNot(QueryBuilders.termQuery("base", ""));
            } else if (StringUtils.isNotBlank(base)) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("base", base));
            }

            if ("unmounted".equals(resourceType)) {
                //查询无资源的目录
                boolQueryBuilder.must(QueryBuilders.termQuery("api", 0)).
                        must(QueryBuilders.termQuery("db", 0)).
                        must(QueryBuilders.termQuery("file", 0));
            }else  if (!"all".equals(resourceType)) {
                //查询有资源的目录
                boolQueryBuilder.must(QueryBuilders.rangeQuery(resourceType).gt(0));
            }

        }
        return boolQueryBuilder;
    }

    /**
     * 更新.
     */
    public static boolean updataData(String esIndex, String esType, String dataId, Map<String, Object> map)
            throws Exception {
     
        boolean booleanResult = true;

        UpdateRequestBuilder urb = client.prepareUpdate(esIndex, esType, dataId).
                setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        urb.setDoc(map);
        try {
            UpdateResponse response = urb.execute().actionGet();
            booleanResult = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新ES失败", map.toString());
            booleanResult = false;
        }
        return booleanResult;
    }
    /**
     * 根据目录id获取目录信息
     */
    public static  Map getCatalogInfo(String index, String catalogId) {
     
        GetResponse response = client.prepareGet(index, index, catalogId)
                .setOperationThreaded(false)    // 线程安全
                .get();
        Map map = response.getSource();

        return map;
    }
    /**
     * 根据目录id获取目录访问量
     */
    public static  Integer getCatalogCount(String index, String catalogId, String resourceType) {
        TransportClient client = EsClient.getClient();
        GetResponse response = client.prepareGet(index, index, catalogId)
                .setOperationThreaded(false)    // 线程安全
                .get();
        JSONObject object = new JSONObject().fromObject(response.getSource());//将json字符串转换为json对象
        Integer catalogCount = object.getInt(resourceType);
        return catalogCount;
    }
    /**
     * 根据id更新数据目录的访问量
     */
    public static void updateCatalog(String index, String catalogId, String resourceType, Object catalogCount) {
        XContentBuilder source = null;
        TransportClient client = EsClient.getClient();
        try {
            source = XContentFactory.jsonBuilder()
                    .startObject()
                    .field(resourceType, catalogCount) //resourceType：要修改的字段名，catalogCount 修改的值
                    .endObject();
            UpdateResponse updateResponse = client.prepareUpdate(index, index, catalogId).setDoc(source).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static SearchHits getSearchHits(String tableName, String field, String fieldValue, String year) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //根据字段筛选
        if (StringUtils.isNotBlank(field)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(field, fieldValue));
        }
        //筛选年份数据
        if (StringUtils.isNotBlank(year)) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("year", year));
        }
        SearchRequestBuilder builder = client.prepareSearch(tableName).setTypes(tableName).
                setQuery(boolQueryBuilder);

        SearchResponse searchResponse = builder.get();

        return searchResponse.getHits();
    }

    public static List<Map<String ,Object>> getDocumentList(String tableName,String field,
                                                            String fieldValue,String year) {
        List<Map<String ,Object>> list = new ArrayList<>();
    SearchHits hits = getSearchHits(tableName,field,fieldValue,year);
    SearchHit[] hits1 = hits.getHits();
        Arrays.stream(hits1).forEach(hit -> {
            list.add(hit.getSource());
        });
    return list;
    }
    //更新索引的max_result_window参数
    public static boolean updateIndex( String index, int indexMaxResultCount) {
        UpdateSettingsResponse indexResponse = client.admin().indices()
                .prepareUpdateSettings(index)
                .setSettings(Settings.builder()
                        .put("index.max_result_window", indexMaxResultCount)
                        .build()
                ).get();
        return indexResponse.isAcknowledged();
    }
}
