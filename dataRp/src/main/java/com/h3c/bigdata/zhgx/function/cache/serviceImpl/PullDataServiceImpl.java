package com.h3c.bigdata.zhgx.function.cache.serviceImpl;

import com.h3c.bigdata.zhgx.function.cache.dao.CacheManagerMapper;
import com.h3c.bigdata.zhgx.function.cache.entity.CacheEntity;
import com.h3c.bigdata.zhgx.function.cache.service.PullDataService;
import com.h3c.bigdata.zhgx.function.system.dao.DictTypeMapper;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.entity.DictType;
import com.h3c.bigdata.zhgx.function.system.entity.SysParamaterEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @desc 缓存处理类
 * @return
 */
@Service
@Transactional
public class PullDataServiceImpl implements PullDataService {
	private static final Log log = LogFactory.getLog(PullDataServiceImpl.class);
	@Autowired
	private CacheManagerMapper mapper;
	@Autowired
	private DictTypeMapper dictTypeMapper;

	private static List<CacheEntity> cacheTableInfoList = new ArrayList<CacheEntity>();
/*
	public void pullData(ConcurrentMap<String, String> cache) {
		log.info("加载系统参数开始!");
		try {
			// 从数据库获取数据，并保存数据
			List<SysParamaterEntity> list = sysParamaterMapper.selectSysParaList(null);
		} catch (Exception e) {
			log.error("加载系统参数异常", e);
		}
		log.info("缓存管理-->服务启动:添加缓存入口调用完成!");
	}*/

	private void SaveToCacheManager(ConcurrentMap<String, CacheEntity> cache) {
		for (CacheEntity ce : cacheTableInfoList) {
			String key = ce.getKey();
			cache.put(key, ce);
		}
		cacheTableInfoList.clear();
	}
	/**
	 * @Description: 针对配置需要独立成一个map的缓存数据进行遍历，并保存到全局map中
	 * @date 2017-6-8
	 */
	private void cacheDataToMap(CacheEntity ce, List<DictData> list) {
		if (null == list || list.size() == 0) {
//			log.info("缓存管理-->添加缓存:字典表没有查询出有效的数据返回!");
			return;
		}
			Map<String, Object> tmp = new HashMap<String, Object>();
		    List<Map<String,Object>> typeList = (List<Map<String,Object>>)tmp.get(ce.getKey());
			// 遍历所有的数据，并把数据添加到临时map中
			for (DictData dictData : list) {
				String dictLabel = dictData.getDictLabel();
				String dictValue = dictData.getDictValue();
				String dictType = dictData.getDictType();
				Integer dictSort = dictData.getDictSort();
				Map<String ,Object> temp = new HashMap<>();
				if(typeList==null){
					typeList = new ArrayList<>();
				}
				temp.put("dictValue",dictValue);
				temp.put("dictLabel",dictLabel);
				temp.put("dictSort",dictSort);
				temp.put("dictType",dictType);
				typeList.add(temp);
				// 改组字段遍历完成，把临时map添加到全局对象中
			}
		    ce.getCacheMapData().put(ce.getKey(), typeList);
	}

	private void getDataFromDBSaveToTempCache() {
		for (CacheEntity ce : cacheTableInfoList) {
			try {
				// 查询数据
				List<DictData> list = mapper.getDataFromDBSaveToTempCache(ce);
				if (null == ce.getList()) {
					ce.setList(new ArrayList<DictData>());
				}
				// 添加全量数据到缓存中
				ce.getList().addAll(list);
				// 把需要独立成map的字段遍历，并添加到临时map中
				cacheDataToMap(ce, list);
			} catch (Exception e) {
				log.error("缓存管理-->添加缓存:查询数据库报错", e);
			}
		}
	}
	/**
	 * 解析缓存的xml文件
	 */
	private void queryDicType() {
		List<DictType> dictTypeList= dictTypeMapper.selectDictTypeList(null);
		for (DictType dt:dictTypeList){
			CacheEntity ce = new CacheEntity();
			ce.setKey(dt.getDictType());
			cacheTableInfoList.add(ce);
		}
	}
	/**
	 * 刷新缓存中的数据
	 */
	@Override
	public void refreshCacheData(ConcurrentMap<String, CacheEntity> cache) {
//		log.info("缓存管理-->刷新缓存:缓存刷新开启调用!");
		try {
			queryDicType();
			// 从数据库获取数据，并保存数据
			getDataFromDBSaveToTempCache();
			// 放入缓存中
			SaveToCacheManager(cache);
		} catch (Exception e) {
			log.error("缓存管理-->刷新缓存:缓存刷新出现异常", e);
		}
//		log.info("缓存管理-->刷新缓存:缓存刷新调用完成!");
	}
}