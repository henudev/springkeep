package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;
import com.h3c.bigdata.zhgx.function.system.model.LogInLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.model.LoginLogResultBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统访问日志记录 数据层
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog>
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
	List<LoginLogResultBean> selectLoginLogList(LogInLogQueryBean logInLogQueryBean);
	
	/**
     * 新增系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	int insertLoginLog(LoginLog loginLog);
	
	/**
     * 修改系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	int updateLoginLog(LoginLog loginLog);
	
	/**
     * 删除系统访问日志记录
     * 
     * @param id 系统访问日志记录ID
     * @return 结果
     */
	int deleteLoginLogById(String id);
	
	/**
     * 批量删除系统访问日志记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteLoginLogByIds(String[] ids);
	
}