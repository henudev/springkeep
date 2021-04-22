package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.model.OperateLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.service.IOperateLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统操作日志记录 信息操作处理
 * 
 * @author j16898
 * @date 2018-07-30
 */
@RestController
@RequestMapping("/system/operateLog")
@LoginOpen
public class OperateLogController extends BaseController
{
	@Autowired
	private IOperateLogService operateLogService;
	
    /**
     * 查询系统操作日志记录列表
     */
	@LoginOpen
    @ApiOperation(value = "系统操作日志记录 信息操作处理", notes = "系统操作日志记录 信息操作处理")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<?> list(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody OperateLogQueryBean operateLogQueryBean){
//        String userId = getUserIdByToken();
    	try {
//			operateLogQueryBean.setUserId(userId);
            return operateLogService.selectOperateLogList(operateLogQueryBean,page,pageSize,field,dir);
        } catch (Exception e) {
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统操作日志记录查询失败!");
        }
    }


	/**
	 * 删除系统操作日志记录
	 */
	@LoginOpen
	@Log(module = ModuleType.SYSTEM_LOG_OPERATE,action = BusinessType.DELETE)
	@ApiOperation(value = "删除系统操作日志记录", notes = "删除系统操作日志记录")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResult<?> delete(@RequestBody List<String> list){
		try {
			operateLogService.deleteOperateLogByIds(list);
            return ApiResult.success("系统操作日志记录删除成功");
		} catch (Exception e) {
			return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "系统操作日志记录删除失败!");
		}
	}
}
