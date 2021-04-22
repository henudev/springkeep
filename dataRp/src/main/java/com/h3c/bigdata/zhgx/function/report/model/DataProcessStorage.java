package com.h3c.bigdata.zhgx.function.report.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataProcessStorage {

	public volatile static Map<String,Map<String,Object>> dataProcessMap = new ConcurrentHashMap<String,Map<String,Object>>();
}
