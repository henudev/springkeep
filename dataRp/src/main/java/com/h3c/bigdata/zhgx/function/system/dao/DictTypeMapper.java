package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.DictType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统字典类型 数据层
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Repository
public interface DictTypeMapper extends BaseMapper<DictType>
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
	List<DictType> selectDictTypeList(DictType dictType);
	
	/**
     * 新增系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	int insertDictType(DictType dictType);
	
	/**
     * 修改系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	int updateDictType(DictType dictType);
	
	/**
     * 删除系统字典类型
     * 
     * @param id 系统字典类型ID
     * @return 结果
     */
	int deleteDictTypeById(String id);
	
	/**
     * 批量删除系统字典类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteDictTypeByIds(String[] ids);


	/**
	 * 批量关联删除DictType表中的数据
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	int deleteDictDataByDictTypeIds(String[] ids);
}