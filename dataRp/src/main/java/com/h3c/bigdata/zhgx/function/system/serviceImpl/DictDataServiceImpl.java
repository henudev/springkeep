package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;

import com.h3c.bigdata.zhgx.function.system.dao.DictDataMapper;
import com.h3c.bigdata.zhgx.function.system.entity.DictData;
import com.h3c.bigdata.zhgx.function.system.model.DictDataResultBean;
import com.h3c.bigdata.zhgx.function.system.service.IDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统字典数据 服务层实现
 * 
 * @author j16898
 * @date 2018-07-30
 */
@Service
@Transactional
public class DictDataServiceImpl extends BaseService implements IDictDataService
{
	@Autowired
	private DictDataMapper dictDataMapper;

	/**
     * 查询系统字典数据信息
     * 
     * @param id 系统字典数据ID
     * @return 系统字典数据信息
     */
    @Override
	public DictData selectDictDataById(String id)
	{
	    return dictDataMapper.selectDictDataById(id);
	}

	/**
	 * 查询系统字典数据信息-根据字典类型查询
	 *
	 * @param dictData 系统字典数据ID
	 * @return 系统字典数据信息
	 */
	@Override
	public ApiResult<?> selectDictDataByEntity(DictData dictData)
	{
		List<DictDataResultBean> dictDataList= dictDataMapper.selectDictDataList(dictData);
		return ApiResult.success("查询系统字典数据列表成功",dictDataList);
	}
	
	/**
     * 查询系统字典数据列表
     * 
     * @param dictData 系统字典数据信息
     * @return 系统字典数据集合
     */
	@Override
	public ApiResult<?> selectDictDataList(DictData dictData,int page, int pageSize, String field,String dir)
	{
	    //field注意转换
        startPage(page,pageSize,field,dir);
        List<DictDataResultBean> dictDataList= dictDataMapper.selectDictDataList(dictData);
        PageResult result = getDataList(dictDataList);
        return ApiResult.success("查询系统字典数据列表成功",result);
	}

	/**
	 * 查询系统字典数据列表   准确查询
	 *
	 * @param dictData 系统字典数据信息
	 * @return 系统字典数据集合
	 */
	@Override
	public ApiResult<?> selectDictDataListPinpoint(DictData dictData,int page, int pageSize, String field,String dir)
	{
		//field注意转换
		startPage(page,pageSize,field,dir);
		List<DictData> dictDataList= dictDataMapper.select(dictData);
		PageResult result = getDataList(dictDataList);
		return ApiResult.success("查询系统字典数据列表成功",result);
	}
	
    /**
     * 新增系统字典数据
     * 
     * @param dictData 系统字典数据信息
     * @return 结果
     */
    @Override
	public ApiResult<?> insertDictData(DictData dictData)
	{

		//判断字典类型名称是否为空,允许字典类型名称重复
		if (StringUtil.isEmpty(dictData.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型名称为空");
		}

		//判断字典标签是否为空
		if (StringUtil.isEmpty(dictData.getDictLabel())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典标签为空");
		}

		//判断字典键值是否为空
		if (StringUtil.isEmpty(dictData.getDictValue())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典键值为空");
		}

		//判断字典排序是否为空
		if (StringUtil.isNull(dictData.getDictSort())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典排序为空");
		}

		//不允许有非法字符
		if(StringUtil.isSpecialChar(dictData.getDictLabel()) || StringUtil.isSpecialChar(dictData.getDictLabel())
				|| StringUtil.isSpecialChar(dictData.getDictValue()) || StringUtil.isSpecialChar(dictData.getDictSort().toString())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"参数存在非法字符");
		}


		//字典类型不允许重复
		DictData data = new DictData();
		data.setDictType(dictData.getDictType());
		data.setDictValue(dictData.getDictValue());
		if (dictDataMapper.select(data).size() != 0){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_EXIST_ERROR,"字典键值已存在，不允许重复");
		}

		//新增保存到数据库
		try{
			dictData.setId(UUIDUtil.absNumUUID());//主键UUID
			dictData.setCreateTime(new Date());//新增创建时间
			dictData.setUpdateTime(new Date());//新增更新时间
			dictDataMapper.insert(dictData);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}

		return ApiResult.success("系统字典数据新增成功！");
	}

	/**
	 * @Description: 修改系统字典数据
	 * @Param:
	 * @UpdateContent: 判断待修改的字典数据是否存在；同一字典类型下的字典值不允许重复
	 * @Updater: l17503
	 * @UpdateTime: 2019/8/29 11:07
	 */
	@Override
	public ApiResult<?> updateDictData(DictData dictData)
	{
		//判断字典类型名称是否为空,允许字典类型名称重复
		if (StringUtil.isEmpty(dictData.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典类型名称为空");
		}

		//判断字典标签是否为空
		if (StringUtil.isEmpty(dictData.getDictLabel())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典标签为空");
		}

		//判断字典键值是否为空
		if (StringUtil.isEmpty(dictData.getDictValue())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典键值为空");
		}

		//判断字典排序是否为空
		if (StringUtil.isNull(dictData.getDictSort())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_NULL_ERROR, "字典排序为空");
		}

		//不允许有非法字符
		if(StringUtil.isSpecialChar(dictData.getDictLabel()) || StringUtil.isSpecialChar(dictData.getDictLabel())
				|| StringUtil.isSpecialChar(dictData.getDictValue()) || StringUtil.isSpecialChar(dictData.getDictSort().toString())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"参数存在非法字符");
		}

		//判断待修改的字典数据是否存在
		DictData old = dictDataMapper.selectByPrimaryKey(dictData.getId());
		if(null == old){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"待修改的字典数据不存在");
		}
		//字典类型不允许修改并且同一字典类型下的字典值不允许重复
		if(!old.getDictType().equals(dictData.getDictType())){
			return ApiResult.fail(ErrorCode.SYSTEM_PARAM_ILLEGAL_ERROR,"字典类型不允许修改");
		} else if(!old.getDictValue().equals(dictData.getDictValue())){
			DictData data = new DictData();
			data.setDictType(dictData.getDictType());
			data.setDictValue(dictData.getDictValue());
			List<DictData> dictDataList = dictDataMapper.select(data);
			if(null != dictDataList && dictDataList.size() != 0){
				return ApiResult.fail(ErrorCode.SYSTEM_PARAM_EXIST_ERROR,"字典键值已存在，不允许重复");
			}
		}

		//更新保存到数据库
		try{
			dictData.setUpdateTime(new Date());
			dictDataMapper.updateByPrimaryKey(dictData);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return ApiResult.success("系统字典数据更新成功！");
	}

	/**
     * 删除系统字典数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public ApiResult<?> deleteDictDataByIds(List<String> ids)
	{
        String[] strings = new String[ids.size()];
        dictDataMapper.deleteDictDataByIds(ids.toArray(strings));
        return ApiResult.success("系统字典数据删除成功！");
	}



	@Override
	public List<DictData> queryDataTypeList(String[] typeArr) {
		return dictDataMapper.queryDataTypeList(typeArr);
	}

	@Override
	public List<Map<String, String>> queryDictLabelAndValueByTypes(String dictType) {
		return dictDataMapper.queryDictLabelAndValueByTypes(dictType);
	}
}
