package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.OperateLog;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogResultBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统操作日志记录 数据层
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Repository
public interface OperateLogMapper extends BaseMapper<OperateLog>
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
	List<OperateLogResultBean> selectOperateLogList(OperateLogQueryBean operateLogQueryBean);
	
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
     * 删除系统操作日志记录
     * 
     * @param id 系统操作日志记录ID
     * @return 结果
     */
	int deleteOperateLogById(String id);
	
	/**
     * 批量删除系统操作日志记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteOperateLogByIds(String[] ids);
	
}