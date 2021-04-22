package com.h3c.bigdata.zhgx.function.cache.service;


import com.h3c.bigdata.zhgx.function.cache.entity.CacheEntity;

import java.util.concurrent.ConcurrentMap;

/**
 * @desc 缓存处理类
 * @return
 */
public interface PullDataService {

    public void refreshCacheData(ConcurrentMap<String, CacheEntity> cache);
}