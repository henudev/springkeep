package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.ResourceScoreEntity;
import com.h3c.bigdata.zhgx.function.system.model.AvgScoreBean;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceScoreEntityMapper extends BaseMapper<ResourceScoreEntity> {

    AvgScoreBean getAvgScore(ResourceScoreEntity resourceScoreEntity);
}