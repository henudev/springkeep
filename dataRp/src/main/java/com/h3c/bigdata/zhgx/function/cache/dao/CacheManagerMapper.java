package com.h3c.bigdata.zhgx.function.cache.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.cache.entity.CacheEntity;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @desc 查询数据字典
 * @return
 */
@Repository
public interface CacheManagerMapper extends BaseMapper<DictData> {

	public List<DictData> getDataFromDBSaveToTempCache(CacheEntity dto);

}