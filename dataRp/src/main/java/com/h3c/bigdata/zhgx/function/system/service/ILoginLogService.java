package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;
import com.h3c.bigdata.zhgx.function.system.model.LogInLogQueryBean;

import java.util.List;

/**
 * 系统访问日志记录 服务层
 * 
 * @author j16898
 * @date 2018-07-30
 */
public interface ILoginLogService
{
	/**
     * 查询系统访问日志记录信息
     * 
     * @param id 系统访问日志记录ID
     * @return 系统访问日志记录信息
     */
	LoginLog selectLoginLogById(String id);
	
	/**
     * 查询系统访问日志记录列表
     * 
     * @param logInLogQueryBean 系统访问日志记录信息
     * @return 系统访问日志记录集合
     */
    ApiResult<?> selectLoginLogList(LogInLogQueryBean logInLogQueryBean, int page, int pageSize, String field, String dir);
	
	/**
     * 新增系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	ApiResult<?> insertLoginLog(LoginLog loginLog);
	
	/**
     * 修改系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	int updateLoginLog(LoginLog loginLog);
		
	/**
     * 删除系统访问日志记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteLoginLogByIds(List<String> ids);
	
}
