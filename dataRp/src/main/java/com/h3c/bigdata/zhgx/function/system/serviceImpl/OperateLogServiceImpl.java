package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import java.util.List;

import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.function.system.dao.OperateLogMapper;
import com.h3c.bigdata.zhgx.function.system.entity.OperateLog;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogResultBean;
import com.h3c.bigdata.zhgx.function.system.service.IOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志记录 服务层实现
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Service
@Transactional
public class OperateLogServiceImpl extends BaseService implements IOperateLogService
{
	@Autowired
	private OperateLogMapper operateLogMapper;

	/**
     * 查询系统操作日志记录信息
     * 
     * @param id 系统操作日志记录ID
     * @return 系统操作日志记录信息
     */
    @Override
	public OperateLog selectOperateLogById(String id)
	{
	    return operateLogMapper.selectOperateLogById(id);
	}
	
	/**
     * 查询系统操作日志记录列表
     * 
     * @param operateLogQueryBean 系统操作日志记录信息
     * @return 系统操作日志记录集合
     */
	@Override
	public ApiResult<?> selectOperateLogList(OperateLogQueryBean operateLogQueryBean, int page, int pageSize, String field, String dir)
	{
	    //field注意转换
        startPage(page,pageSize,field,dir);
        List<OperateLogResultBean> operateLogList= operateLogMapper.selectOperateLogList(operateLogQueryBean);
        PageResult result = getDataList(operateLogList);
        return ApiResult.success("查询系统操作日志记录列表成功",result);
	}
	
    /**
     * 新增系统操作日志记录
     * 
     * @param operateLog 系统操作日志记录信息
     * @return 结果
     */
	@Override
	public int insertOperateLog(OperateLog operateLog)
	{
        try{
            return operateLogMapper.insert(operateLog);
        }catch (Exception e){
            System.out.println( e.getMessage());
            return 0;
        }
	}


	/**
     * 修改系统操作日志记录
     * 
     * @param operateLog 系统操作日志记录信息
     * @return 结果
     */
	@Override
	public int updateOperateLog(OperateLog operateLog)
	{
        try{
            return operateLogMapper.updateOperateLog(operateLog);
        }catch (Exception e){
            System.out.println( e.getMessage());
            return 0;
        }
	}

	/**
     * 删除系统操作日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOperateLogByIds(List<String> ids)
	{
        try{
            String[] strings = new String[ids.size()];
            return operateLogMapper.deleteOperateLogByIds(ids.toArray(strings));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }

	}
	
}
