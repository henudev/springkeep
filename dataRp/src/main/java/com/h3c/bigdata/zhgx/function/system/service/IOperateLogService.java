package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.OperateLog;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogQueryBean;

import java.util.List;

/**
 * 系统操作日志记录 服务层
 * 
 * @author j16898
 * @date 2018-07-30
 */
public interface IOperateLogService
{
	/**
     * 查询系统操作日志记录信息
     * 
     * @param id 系统操作日志记录ID
     * @return 系统操作日志记录信息
     */
	OperateLog selectOperateLogById(String id);
	
	/**
     * 查询系统操作日志记录列表
     * 
     * @param operateLogQueryBean 系统操作日志记录信息
     * @return 系统操作日志记录集合
     */
    ApiResult<?> selectOperateLogList(OperateLogQueryBean operateLogQueryBean, int page, int pageSize, String field, String dir);
	
	/**
     * 新增系统操作日志记录
     * 
     * @param operateLog 系统操作日志记录信息
     * @return 结果
     */
	int insertOperateLog(OperateLog operateLog);

	/**
     * 修改系统操作日志记录
     * 
     * @param operateLog 系统操作日志记录信息
     * @return 结果
     */
	int updateOperateLog(OperateLog operateLog);
		
	/**
     * 删除系统操作日志记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOperateLogByIds(List<String> ids);
	
}
