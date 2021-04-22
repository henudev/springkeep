package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.DictType;
import com.h3c.bigdata.zhgx.function.system.service.IDictTypeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统字典类型 信息操作处理
 * 
 * @author j16898
 * @date 2018-07-30
 */
@RestController
@RequestMapping("/system/dictType")
@LoginOpen
public class DictTypeController extends BaseController
{
	/**
	 * 日志记录.
	 */
	private Logger log = LoggerFactory.getLogger(DictTypeController.class);

	@Autowired
	private IDictTypeService dictTypeService;
	
    /**
     * 查询系统字典类型列表
     */
	@LoginOpen
    @ApiOperation(value = "系统字典类型 信息操作处理", notes = "系统字典类型 信息操作处理")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<?> list(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody DictType dictType, HttpServletRequest request){
        try {
            return dictTypeService.selectDictTypeList(dictType,page,pageSize,field,dir);
        } catch (Exception e) {
			log.error("系统字典类型查询失败!",e);
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统字典类型查询失败!");
        }
    }

	/**
	 * 查询系统字典类型列表
	 */
	@LoginOpen
	@ApiOperation(value = "系统字典类型 信息操作处理", notes = "系统字典类型 信息操作处理")
	@RequestMapping(value = "/listPinpoint", method = RequestMethod.POST)
	@ResponseBody
	public ApiResult<?> listPinpoint(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody DictType dictType, HttpServletRequest request){
		try {
			return dictTypeService.selectDictTypeListPinpoint(dictType,page,pageSize,field,dir);
		} catch (Exception e) {
			log.error("系统字典类型查询失败!",e);
			return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统字典类型查询失败!");
		}
	}

    /**
     * 新增保存系统字典类型.
     */
	@LoginOpen
	@Log(module = ModuleType.SYSTEM_DICT_TYPE,action = BusinessType.INSERT)
    @ApiOperation(value = "新增保存系统字典类型", notes = "新增保存系统字典类型")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces={"application/json"})
    public ApiResult<?> addSave(@RequestBody DictType dictType){
    	String userId = getUserIdByToken();
        try {
        	dictType.setCreateUser(userId);
			return dictTypeService.insertDictType(dictType);
        } catch (Exception e) {
			log.error("系统字典类型保存失败!",e);
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "系统字典类型保存失败!");
        }
    }


	/**
	 * 更新系统字典类型
	 */
	@LoginOpen
	@Log(module = ModuleType.SYSTEM_DICT_TYPE,action = BusinessType.UPDATE)
	@ApiOperation(value = "更新系统字典类型", notes = "更新系统字典类型.")
	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	public ApiResult<?> edit(@RequestBody DictType dictType){
		String userId = getUserIdByToken();
		try {
			dictType.setUpdateUser(userId);
			return dictTypeService.updateDictType(dictType);
		} catch (Exception e) {
			log.error("系统字典类型更新失败!",e);
			return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "系统字典类型更新失败!");
		}
	}

	/**
	 * 删除系统字典类型
	 */
	@LoginOpen
	@Log(module = ModuleType.SYSTEM_DICT_TYPE,action = BusinessType.DELETE)
	@ApiOperation(value = "删除系统字典类型", notes = "删除系统字典类型")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResult<?> delete(@RequestBody List<String> list){
		try {
			return dictTypeService.deleteDictTypeByIds(list);
		} catch (Exception e) {
			log.error("系统字典类型删除失败!",e);
			return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "系统字典类型删除失败!");
		}
	}
}
