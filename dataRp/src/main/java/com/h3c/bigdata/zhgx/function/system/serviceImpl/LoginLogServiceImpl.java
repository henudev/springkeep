package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import java.util.Date;
import java.util.List;

import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.function.system.dao.LoginLogMapper;
import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;
import com.h3c.bigdata.zhgx.function.system.model.LogInLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.model.LoginLogResultBean;
import com.h3c.bigdata.zhgx.function.system.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统访问日志记录 服务层实现
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Service
@Transactional
public class LoginLogServiceImpl extends BaseService implements ILoginLogService
{
	@Autowired
	private LoginLogMapper loginLogMapper;

	/**
     * 查询系统访问日志记录信息
     * 
     * @param id 系统访问日志记录ID
     * @return 系统访问日志记录信息
     */
    @Override
	public LoginLog selectLoginLogById(String id)
	{
	    return loginLogMapper.selectLoginLogById(id);
	}
	
	/**
     * 查询系统访问日志记录列表
     * 
     * @param logInLogQueryBean 系统访问日志记录信息
     * @return 系统访问日志记录集合
     */
	@Override
	public ApiResult<?> selectLoginLogList(LogInLogQueryBean logInLogQueryBean, int page, int pageSize, String field, String dir)
	{
	    //field注意转换
        startPage(page,pageSize,field,dir);
        List<LoginLogResultBean> loginLogList= loginLogMapper.selectLoginLogList(logInLogQueryBean);
        PageResult result = getDataList(loginLogList);
        return ApiResult.success("查询系统访问日志记录列表成功",result);
	}
	
    /**
     * 新增系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	@Override
	public ApiResult<?> insertLoginLog(LoginLog loginLog)
	{
		loginLog.setId(UUIDUtil.absNumUUID());
		loginLog.setLoginTime(new Date());
		loginLogMapper.insert(loginLog);
		return ApiResult.success("登录日志新增成功！");
	}
	
	/**
     * 修改系统访问日志记录
     * 
     * @param loginLog 系统访问日志记录信息
     * @return 结果
     */
	@Override
	public int updateLoginLog(LoginLog loginLog)
	{
        try{
            return loginLogMapper.updateLoginLog(loginLog);
        }catch (Exception e){
            System.out.println( e.getMessage());
            return 0;
        }
	}

	/**
     * 删除系统访问日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteLoginLogByIds(List<String> ids)
	{
        try{
            String[] strings = new String[ids.size()];
            return loginLogMapper.deleteLoginLogByIds(ids.toArray(strings));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }

	}
}
