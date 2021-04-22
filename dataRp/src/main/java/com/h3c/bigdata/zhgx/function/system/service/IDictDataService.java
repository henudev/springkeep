package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;

import com.h3c.bigdata.zhgx.function.system.entity.DictData;

import java.util.List;
import java.util.Map;

/**
 * 系统字典数据 服务层
 * 
 * @author j16898
 * @date 2018-07-30
 */
public interface IDictDataService
{
	/**
     * 查询系统字典数据信息
     * 
     * @param id 系统字典数据ID
     * @return 系统字典数据信息
     */
	DictData selectDictDataById(String id);

	/**
	 * 查询系统字典数据信息-根据字典类型
	 *
	 * @param dictData 系统字典数据
	 * @return 系统字典数据信息
	 */
	ApiResult<?> selectDictDataByEntity(DictData dictData);
	
	/**
     * 查询系统字典数据列表
     * 
     * @param dictData 系统字典数据信息
     * @return 系统字典数据集合
     */
    ApiResult<?> selectDictDataList(DictData dictData, int page, int pageSize, String field, String dir);

	/**
	 * 查询系统字典数据列表    准确查询
	 * @param dictData
	 * @param page
	 * @param pageSize
	 * @param field
	 * @param dir
	 * @return
	 */
	ApiResult<?> selectDictDataListPinpoint(DictData dictData, int page, int pageSize, String field, String dir);

	/**
     * 新增系统字典数据
     * 
     * @param dictData 系统字典数据信息
     * @return 结果
     */
	ApiResult<?> insertDictData(DictData dictData);
	
	/**
     * 修改系统字典数据
     * 
     * @param dictData 系统字典数据信息
     * @return 结果
     */
	ApiResult<?> updateDictData(DictData dictData);
		
	/**
     * 删除系统字典数据信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	ApiResult<?> deleteDictDataByIds(List<String> ids);


	/**
	 * 根据 typeArr对应类型 返回所有的list
	 * @param typeArr
	 * @return
	 */
	List<DictData> queryDataTypeList(String[] typeArr);

	/**
	 *  根据dictType 获取dictLabel和dictValue
	 * @param dictType
	 * @return
	 */
	List<Map<String, String>> queryDictLabelAndValueByTypes(String dictType);
}
