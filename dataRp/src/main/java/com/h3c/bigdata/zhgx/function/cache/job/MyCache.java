package com.h3c.bigdata.zhgx.function.cache.job;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.cache.entity.CacheEntity;
import com.h3c.bigdata.zhgx.function.cache.service.PullDataService;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class MyCache implements CommandLineRunner {
    private static final Log log = LogFactory.getLog(MyCache.class);
    private static ConcurrentMap<String, CacheEntity> CACHE = new ConcurrentHashMap<String, CacheEntity>();
    @Autowired
    private PullDataService pullDataService;
    //每5分钟执行一次（单位秒）
    protected String PERIOD = "300000000";
    // 定时任务启动之后间隔时长开始执行定时任务
    private final int INITIAL_DELAY = 10;
    // 定时任务的执行周期(单位：毫秒)
    private final int PERIOD_TMP = 5 * 600000;
    @Override
    public void run(String... args) throws Exception {
//        log.info("缓存管理-->缓存初始化:服务启动!");
        refreshCacheData();
//        log.info(CACHE.toString());
//        log.info("缓存管理-->缓存初始化:服务加载结束!");
    }
    /**
     * @param
     * @Description: 定时刷新缓存数据
     */
    private void refreshCacheData() {
        ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();
        if (StringUtils.isEmpty(PERIOD)) {
            // 当数据库配置的为空时走默认5分钟
            executors.scheduleAtFixedRate(new CacheDataRefresh(), INITIAL_DELAY, PERIOD_TMP, TimeUnit.SECONDS);
        } else {
            executors.scheduleAtFixedRate(new CacheDataRefresh(), INITIAL_DELAY, Integer.parseInt(PERIOD),
                    TimeUnit.SECONDS);
        }
    }
    // 获取某个缓存
    public ApiResult<?> getCache(String key) {
        CacheEntity cacheEntity = CACHE.get(key);
        if (cacheEntity == null) {
            return ApiResult.fail("抱歉无数据");
        } else {
            Map<String, Object> map = CACHE.get(key).getCacheMapData();
            return ApiResult.success(CACHE.get(key).getCacheMapData());
        }
    }
    // 缓存添加数据
    public void put(String key, Map<String, DictData> o, CacheEntity ce) {
        List<DictData> list = CACHE.get(key).getList();
        if (list != null) {
        } else {
            CACHE.put(key, ce);
        }
    }
    /**
     * @Description: 刷新缓存定时处理类
     */
    private class CacheDataRefresh implements Runnable {
        @Override
        public void run() {
            try {
                pullDataService.refreshCacheData(CACHE);
//                log.info("刷新缓存数据 :" + CACHE.toString());
//                log.info("刷新缓存数据 :");
            } catch (Exception e) {
                log.error("缓存管理-->刷新缓存:定时调用刷新服务出现异常", e);
            }
        }
    }

    public static ConcurrentMap<String, CacheEntity> getCACHE() {
        return CACHE;
    }
}
