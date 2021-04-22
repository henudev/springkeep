package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import java.util.Date;
import java.util.List;

import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.function.system.dao.DictTypeMapper;
import com.h3c.bigdata.zhgx.function.system.entity.DictType;
import com.h3c.bigdata.zhgx.function.system.service.IDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统字典类型 服务层实现
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Service
@Transactional
public class DictTypeServiceImpl extends BaseService implements IDictTypeService
{
	@Autowired
	private DictTypeMapper dictTypeMapper;

	/**
     * 查询系统字典类型信息
     * 
     * @param id 系统字典类型ID
     * @return 系统字典类型信息
     */
    @Override
	public DictType selectDictTypeById(String id)
	{
	    return dictTypeMapper.selectDictTypeById(id);
	}
	
	/**
     * 查询系统字典类型列表
     * 
     * @param dictType 系统字典类型信息
     * @return 系统字典类型集合
     */
	@Override
	public ApiResult<?> selectDictTypeList(DictType dictType,int page, int pageSize, String field,String dir)
	{
	    //field注意转换
        startPage(page,pageSize,field,dir);
        List<DictType> dictTypeList= dictTypeMapper.selectDictTypeList(dictType);
        PageResult result = getDataList(dictTypeList);
        return ApiResult.success("查询系统字典类型列表成功",result);
	}

	/**
	 * 查询系统字典类型列表
	 *
	 * @param dictType 系统字典类型信息
	 * @return 系统字典类型集合
	 */
	@Override
	public ApiResult<?> selectDictTypeListPinpoint(DictType dictType,int page, int pageSize, String field,String dir)
	{
		//field注意转换
		startPage(page,pageSize,field,dir);
		List<DictType> dictTypeList= dictTypeMapper.select(dictType);
		PageResult result = getDataList(dictTypeList);
		return ApiResult.success("查询系统字典类型列表成功",result);
	}
	
    /**
     * 新增系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	@Override
	public ApiResult<?> insertDictType(DictType dictType)
	{

		//判断字典类型名称是否为空,允许字典类型名称重复
		if (StringUtil.isEmpty(dictType.getDictName())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型名称为空");
		}

		//判断字典类型是否为空
		if (StringUtil.isEmpty(dictType.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型为空");
		}

		//不允许有非法字符
		if(StringUtil.isSpecialChar(dictType.getDictName()) || StringUtil.isSpecialChar(dictType.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"参数存在非法字符");
		}

		//判断长度合法性
		if(dictType.getDictType().length()>30||dictType.getDictName().length()>30){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_EXIST_ERROR,"参数长度过长");
		}


		//字典类型不允许重复
		DictType type = new DictType();
		type.setDictType(dictType.getDictType());
		if (dictTypeMapper.select(type).size() != 0){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_EXIST_ERROR,"字典类型已存在，不允许重复");
		}

		//新增保存到数据库
		dictType.setId(UUIDUtil.absNumUUID());//主键UUID
		dictType.setCreateTime(new Date());//新增创建时间
		dictType.setUpdateTime(new Date());//新增更新时间
		dictTypeMapper.insert(dictType);


		return ApiResult.success("系统字典类型新增成功！");
	}
	
	/**
     * 修改系统字典类型
     * 
     * @param dictType 系统字典类型信息
     * @return 结果
     */
	@Override
	public ApiResult<?> updateDictType(DictType dictType)
	{
		//判断字典类型名称是否为空,允许字典类型名称重复
		if (StringUtil.isEmpty(dictType.getDictName())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型名称为空");
		}

		//判断字典类型是否为空
		if (StringUtil.isEmpty(dictType.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型为空");
		}

		//不允许有非法字符
		if(StringUtil.isSpecialChar(dictType.getDictName()) || StringUtil.isSpecialChar(dictType.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"参数存在非法字符");
		}

		//字典类型不允许重复
		DictType type = new DictType();
		type.setDictType(dictType.getDictType());
		List<DictType> dictTypeList = dictTypeMapper.select(type);
		int size = dictTypeList.size();
		/**
		 * size等于0，必然不重复；
		 * size大于1，肯定是重复；
		 * size等于1，判断是不是当前对象，利用ID匹配；
		 */
		if (size > 1 || (size == 1 && !dictTypeList.get(0).getId().equals(dictType.getId()))){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_EXIST_ERROR,"字典类型已存在，不允许重复");
		}

		//更新保存到数据库
		try{
			dictType.setUpdateTime(new Date());
			dictTypeMapper.updateByPrimaryKey(dictType);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}

		return ApiResult.success("系统字典类型更新成功！");
	}

	/**
     * 删除系统字典类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public ApiResult<?> deleteDictTypeByIds(List<String> ids)
	{
		String[] strings = new String[ids.size()];
        dictTypeMapper.deleteDictDataByDictTypeIds(ids.toArray(strings));
		dictTypeMapper.deleteDictTypeByIds(ids.toArray(strings));
		return ApiResult.success("系统字典类型删除成功！");
	}


}
