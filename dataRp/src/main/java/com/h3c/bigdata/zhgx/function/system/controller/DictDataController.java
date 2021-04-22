package com.h3c.bigdata.zhgx.function.system.controller;

import com.h3c.bigdata.zhgx.common.constant.BusinessType;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.constant.ModuleType;
import com.h3c.bigdata.zhgx.common.log.Log;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.annotation.LoginOpen;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.service.IDictDataService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统字典数据 信息操作处理
 * 
 * @author j16898
 * @date 2018-07-30
 */
@RestController
@RequestMapping("/system/dictData")
@LoginOpen
public class DictDataController extends BaseController
{
    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(DictDataController.class);

	@Autowired
	private IDictDataService dictDataService;
	
    /**
     * 查询系统字典数据列表
     */
    @ApiOperation(value = "系统字典数据 信息操作处理", notes = "系统字典数据 信息操作处理")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<?> list(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody DictData dictData){
        try {
            return dictDataService.selectDictDataList(dictData,page,pageSize,field,dir);
        } catch (Exception e) {
            log.error("系统字典数据查询失败!",e);
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统字典数据查询失败!");
        }
    }

	/**
	 * 查询系统字典数据列表
	 */

	@ApiOperation(value = "系统字典数据 信息操作处理", notes = "系统字典数据 信息操作处理")
	@RequestMapping(value = "/listPinpoint", method = RequestMethod.POST)
	@ResponseBody
	public ApiResult<?> listPinpoint(@RequestParam(value = "page", defaultValue = "1") int page,
							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							 @RequestParam(value = "field", defaultValue = "id") String field,
							 @RequestParam(value = "dir", defaultValue = "asc") String dir,
							 @RequestBody DictData dictData, HttpServletRequest request){
		try {
			return dictDataService.selectDictDataListPinpoint(dictData,page,pageSize,field,dir);
		} catch (Exception e) {
			log.error("系统字典数据查询失败!",e);
			return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统字典数据查询失败!");
		}
	}

	/**
	 * 查询系统字典数据列表-
	 */

	@ApiOperation(value = "系统字典数据 根据系统类型查询", notes = "系统字典数据 根据系统类型查询")
	@RequestMapping(value = "/queryByDataType", method = RequestMethod.POST)
	@ResponseBody
	public ApiResult<?> queryByDataType(@RequestBody DictData dictData, HttpServletRequest request){
		try {
			return dictDataService.selectDictDataByEntity(dictData);
		} catch (Exception e) {
			log.error("系统字典数据查询失败!",e);
			return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "系统字典数据查询失败!");
		}
	}
	
    /**
     * 新增保存系统字典数据.
     */

	@Log(module = ModuleType.SYSTEM_DICT_DATA,action = BusinessType.INSERT)
    @ApiOperation(value = "新增保存系统字典数据", notes = "新增保存系统字典数据")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces={"application/json"})
    public ApiResult<?> addSave(@RequestBody DictData dictData, HttpServletRequest request){
		String userId = getUserIdByToken();
		try {
			dictData.setCreateUser(userId);
			return dictDataService.insertDictData(dictData);
        } catch (Exception e) {
            log.error("系统字典数据保存失败!",e);
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "系统字典数据保存失败!");
        }
    }


	/**
	 * 更新系统字典数据
	 */

	@Log(module = ModuleType.SYSTEM_DICT_DATA,action = BusinessType.UPDATE)
	@ApiOperation(value = "更新系统字典数据", notes = "更新系统字典数据.")
	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	public ApiResult<?> edit(@RequestBody DictData dictData, HttpServletRequest request){
		try {
			return dictDataService.updateDictData(dictData);
		} catch (Exception e) {
            log.error("系统字典数据更新失败!",e);
			return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "系统字典数据更新失败!");
		}
	}

	/**
	 * 删除系统字典数据
	 */

	@Log(module = ModuleType.SYSTEM_DICT_DATA,action = BusinessType.DELETE)
	@ApiOperation(value = "删除系统字典数据", notes = "删除系统字典数据")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResult<?> delete(@RequestBody List<String> list){
		try {
			return dictDataService.deleteDictDataByIds(list);
		} catch (Exception e) {
            log.error("系统字典数据删除失败!",e);
			return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "系统字典数据删除失败!");
		}
	}
}
