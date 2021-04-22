package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.DepartmentInfoResultBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthDepartmentInfoEntityMapper extends BaseMapper<AuthDepartmentInfoEntity> {

    /**
     * 查询某节点下最大code的节点
     * @param map
     * @return
     */
    List<AuthDepartmentInfoEntity> queryMaxDptCodeByParentCode(Map<String, Object> map);

    /**
     * 查询某节点下最大code的节点
     * @param authDepartmentInfoEntity
     * @return
     */
    List<DepartmentInfoResultBean> queryDptInfoEntityList(AuthDepartmentInfoEntity authDepartmentInfoEntity);

    /**
     * 查询其下“子节点”
     * @param list
     * @return
     */
    List<AuthDepartmentInfoEntity> queryChildrenDptsByParentId(@Param("list") List<AuthDepartmentInfoEntity> list);

    /**
     * 删除、批量删除组织部门
     *
     * @param
     * @return
    */
    void deleteDptsStatus(@Param("list") List<AuthDepartmentInfoEntity> list);



    AuthDepartmentInfoEntity selectDepartmentById(@Param("id") String id);

    /**
     * 查询登陆人员所在的部门信息
     * @return
     */
    AuthDepartmentInfoEntity selectDeptByUserId(@Param("userId") String userId);

    /**
     * @Description: 依据departmentCode查询部门信息
     * @Author: l17503
     * @Date: 2019/7/22 16:24
     */
    AuthDepartmentInfoEntity selectByDepartmentCode(@Param("departmentCode") String departmentCode);


    /**
    * @Description: 获取子部门的id列表
    * @param
    * @return
    * @Author: w15112
    * @Date: 2019/8/22 11:43
    */
    List<String> selectDepartmentIdsByCode(@Param("departmentCode") String departmentCode);

    /**
     * 获取子部门code列表
     *
     * @param departmentCode
     * @return
     */
    List<String> selectDepartmentCodesByCode(@Param("departmentCode") String departmentCode);

    /**
     * @Description: 依据roleKey查询部门列表（主要用于查询roleKey=0）
     * @Param:
     * @Author: l17503
     * @Date: 2019/12/20 17:50
     */
    List<AuthDepartmentInfoEntity> selectByRoleKey(@Param("roleKey") String roleKey);

    /**
     * 根据部门名称获得部门ID
     *
     * @param departmentName
     * @return
     */
    @Select("select id from auth_department_info where department_name=#{departmentName}")
    String selectDepartmentIdByDeptName(@Param("departmentName") String departmentName);

}