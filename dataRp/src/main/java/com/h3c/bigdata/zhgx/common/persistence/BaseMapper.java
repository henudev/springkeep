package com.h3c.bigdata.zhgx.common.persistence;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Mingchao.Ji on 18/03/25.
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>{
}
