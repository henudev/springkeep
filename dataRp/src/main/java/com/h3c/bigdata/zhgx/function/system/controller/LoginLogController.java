package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.model.LogInLogQueryBean;
import com.h3c.bigdata.zhgx.function.system.service.ILoginLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问日志记录 信息操作处理
 * 
 * @author j16898
 * @date 2018-07-30
 */
@RestController
@RequestMapping("/system/loginLog")
@LoginOpen
public class LoginLogController extends BaseController
{
	@Autowired
	private ILoginLogService loginLogService;
	
    /**
     * 查询系统访问日志记录列表
     */
	@LoginOpen
    @ApiOperation(value = "系统访问日志记录 信息操作处理", notes = "系统访问日志记录 信息操作处理")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<?> list(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody LogInLogQueryBean logInLogQueryBean){
		String userId = getUserIdByToken();
		try {
			logInLogQueryBean.setLoginUserId(userId);
            return loginLogService.selectLoginLogList(logInLogQueryBean,page,pageSize,field,dir);
        } catch (Exception e) {
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统访问日志记录查询失败!");
        }
    }
	
//    /**
//     * 新增保存系统访问日志记录.
//     */
//    @ApiOperation(value = "新增保存系统访问日志记录", notes = "新增保存系统访问日志记录")
//    @RequestMapping(value = "/add", method = RequestMethod.POST, produces={"application/json"})
//    public ApiResult<?> addSave(@RequestBody LoginLog loginLog, HttpServletRequest request1){
//        try {
//			return loginLogService.insertLoginLog(loginLog);
//        } catch (Exception e) {
//            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "系统访问日志记录保存失败!");
//        }
//    }
//
//
//	/**
//	 * 更新系统访问日志记录
//	 */
//	@ApiOperation(value = "更新系统访问日志记录", notes = "更新系统访问日志记录.")
//	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
//	public ApiResult<?> edit(@RequestBody LoginLog loginLog, HttpServletRequest request){
//		try {
//			loginLogService.updateLoginLog(loginLog);
//            return ApiResult.success("系统访问日志记录更新成功");
//		} catch (Exception e) {
//			return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "系统访问日志记录更新失败!");
//		}
//	}

	/**
	 * 删除系统访问日志记录
	 */
	@LoginOpen
	@Log(module = ModuleType.SYSTEM_LOG_LOGIN,action = BusinessType.DELETE)
	@ApiOperation(value = "删除系统访问日志记录", notes = "删除系统访问日志记录")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResult<?> delete(@RequestBody List<String> list){
		try {
			loginLogService.deleteLoginLogByIds(list);
            return ApiResult.success("系统访问日志记录删除成功");
		} catch (Exception e) {
			return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "系统访问日志记录删除失败!");
		}
	}
}
