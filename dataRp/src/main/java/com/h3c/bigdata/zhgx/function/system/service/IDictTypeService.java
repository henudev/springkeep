package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.DictType;

import java.util.List;

/**
 * 系统字典类型 服务层
 * 
 * @author j16898
 * @date 2018-07-30
 */
public interface IDictTypeService
{
	/**
     * 查询系统字典类型信息
     * 
     * @param id 系统字典类型ID
     * @return 系统字典类型信息
     */
	DictType selectDictTypeById(String id);
	
	/**
     * 查询系统字典类型列表
     * 
     * @param dictType 系统字典类型信息
     * @return 系统字典类型集合
     */
    ApiResult<?> selectDictTypeList(DictType dictType, int page, int pageSize, String field, String dir);

	/**
	 * 查询系统字典类型列表  准确查
	 * @param dictType
	 * @param page
	 * @param pageSize
	 * @param field
	 * @param dir
	 * @return
	 */
	ApiResult<?> selectDictTypeListPinpoint(DictType dictType, int page, int pageSize, String field, String dir);
	
	/**
     * 新增系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	ApiResult<?> insertDictType(DictType dictType);
	
	/**
     * 修改系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	ApiResult<?> updateDictType(DictType dictType);
		
	/**
     * 删除系统字典类型信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	ApiResult<?> deleteDictTypeByIds(List<String> ids);



}
