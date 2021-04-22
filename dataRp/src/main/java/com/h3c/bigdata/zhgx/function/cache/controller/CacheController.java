package com.h3c.bigdata.zhgx.function.cache.controller;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.function.cache.job.MyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @desc 测试缓存加载数据
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

	@Autowired
	private MyCache myCache;

	@LoginOpen
	@ResponseBody
	@RequestMapping(value="/getDicData", method = RequestMethod.POST)
	public ApiResult<?> getDicData(@RequestParam(value = "dicType", required = true)String dicType) {
		return myCache.getCache(dicType);
	}
}
