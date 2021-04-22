package com.h3c.bigdata.zhgx.function.cache.entity;

import com.h3c.bigdata.zhgx.function.system.entity.DictData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc 缓存实体类
 * @return
 */
public class CacheEntity {

	private String key;
	private Map<String, Object> cacheMapData = new HashMap<String,Object>();
	private List<DictData> list = new ArrayList<DictData>();

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取list
	 *
	 * @return list list
	 */
	public List<DictData> getList() {
		return list;
	}

	/**
	 * 设置list
	 *
	 * @param list list
	 */
	public void setList(List<DictData> list) {
		this.list = list;
	}

	/**
	 * 获取cacheMapData
	 *
	 * @return cacheMapData cacheMapData
	 */
	public Map<String, Object> getCacheMapData() {
		return cacheMapData;
	}

	/**
	 * 设置cacheMapData
	 *
	 * @param cacheMapData cacheMapData
	 */
	public void setCacheMapData(Map<String, Object> cacheMapData) {
		this.cacheMapData = cacheMapData;
	}


	@Override
	public String toString() {
		return "CacheEntity{" +
				"key='" + key + '\'' +
				", cacheMapData=" + cacheMapData +
				", list=" + list +
				'}';
	}
}