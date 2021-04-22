package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.model.DictDataResultBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统字典数据 数据层
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Repository
public interface DictDataMapper extends BaseMapper<DictData>
{
	/**
     * 查询系统字典数据信息
     * 
     * @param id 系统字典数据ID
     * @return 系统字典数据信息
     */
	DictData selectDictDataById(String id);

	/**
	 * 根据type和value查询
	 */
	DictData getDictDataByTypeValue(@Param("dictType")String dictType,@Param("dictValue")String dictValue);
	
	/**
     * 查询系统字典数据列表
     * 
     * @param dictData 系统字典数据信息
     * @return 系统字典数据集合
     */
	List<DictDataResultBean> selectDictDataList(DictData dictData);
	
	/**
     * 新增系统字典数据
     * 
     * @param dictData 系统字典数据信息
     * @return 结果
     */
	int insertDictData(DictData dictData);
	
	/**
     * 修改系统字典数据
     * 
     * @param dictData 系统字典数据信息
     * @return 结果
     */
	int updateDictData(DictData dictData);
	
	/**
     * 删除系统字典数据
     * 
     * @param id 系统字典数据ID
     * @return 结果
     */
	int deleteDictDataById(String id);
	
	/**
     * 批量删除系统字典数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteDictDataByIds(String[] ids);


	/**
	 * 根据 typeArr对应类型 返回所有的list
	 * @param typeArr
	 * @return
	 */
	List<DictData> queryDataTypeList(String[] typeArr);



    List<Map<String,String>> queryDictLabelAndValueByTypes(@Param("dictType") String dictType);

	@Select("select dict_value as dictValue from sys_dict_data where dict_label=#{dictLabel}")
	String getDictValueByDictLabel(@Param("dictLabel") String dictLabel);
}