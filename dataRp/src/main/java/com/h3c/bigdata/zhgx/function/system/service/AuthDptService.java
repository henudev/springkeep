package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;

import java.util.List;

/**
 * @Author: h17338
 * @Description: 部门维护类接口
 * @Date: 2018/7/30
 */
public interface AuthDptService {

    /**
     * 新增部门信息.
     * @param departmentEntity 查询实体 .
     * @return 部门信息结果信息.
     */
    ApiResult<?> addDptInfo(AuthDepartmentInfoEntity departmentEntity);

    /**
     * 更新部门信息.
     * @param departmentEntity 查询实体 .
     * @return 部门信息结果信息.
     */
    ApiResult<?> updateDptInfo(AuthDepartmentInfoEntity departmentEntity);

    /**
     * 更新部门地理信息.
     * @param departmentEntity 查询实体 .
     * @return 部门信息结果信息.
     */
    ApiResult<?> updateLocDptInfo(AuthDepartmentInfoEntity departmentEntity);
    /**
     * 查询部门信息.
     * @param departmentEntity
     * @return .
     */
    ApiResult<?> queryDptInfo(int page, int pageSize, String field, String dir, AuthDepartmentInfoEntity departmentEntity);

    /**
     * 准确查询部门信息
     * @param departmentInfoEntity
     * @return
     */
    ApiResult<?> queryDptInfoPinpoint(AuthDepartmentInfoEntity departmentInfoEntity,int page, int pageSize, String field, String dir);

    /**
     * 删除、批量删除组织部门
     * @param dpts
     * @return .
     */
    ApiResult<?> deleteDptsStatus(List<AuthDepartmentInfoEntity> dpts);

    /**
     * 数据申请页面的信息 包括用户信息和叶子部门信息
     * @return
     */
    ApiResult getDataApplyInfo(String userId);

    AuthDepartmentInfoEntity selectDeptByUserId(String userId);


    /**
    * @Description: 根据部门id获取本部门及子部门列表
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/8/22 15:20
    */
    List<String> getDepListById(String departmentId);

    List<String> getDeprCodeListByDeptId(String departmentId);


    /**
    * @Description: 普通用户/部门管理员根据用户最小roleKey获取对应的部门及子部门列表,系统管理员/平台开发者为null
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/8/22 16:00
    */
    List<String> getDepListByRoleKey(String departmentId, String userId);

    ApiResult<?> checkDepExistTableOrCategory(String depId);
}
