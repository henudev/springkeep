package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserDeptEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.InsertShowBean;
import com.h3c.bigdata.zhgx.function.system.model.UserDeptRoleBean;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthUserInfoEntityMapper extends BaseMapper<AuthUserInfoEntity> {

    List<AuthUserInfoEntity> selectMaxUserIdByUserNamePinyin(Map<String, Object> map);

    void updateUsersStatus(UsersStatusBean usersStatusBean);

    List<AuthUserInfoEntity> selectUserByDptId(@Param("list") List<AuthDepartmentInfoEntity> list);

    /**
     * 根据角色类型和部门 查询该部门下该角色的用户信息
     *
     * @param deptRoleBean
     * @return
     */
    List<AuthUserInfoEntity> selectUserBy(@Param("deptRoleBean") UserDeptRoleBean deptRoleBean);

    List<AuthUserDeptEntity> selectUserByRoleIdAndDepId(@Param("deptRoleBean") UserDeptRoleBean deptRoleBean);

    List<AuthUserDeptEntity> selectUsersByRoleId(String roleId);

    AuthUserInfoEntity selectUserByUserId(@Param("userId") String userId);

    UserWithRoleModel selectUserModelByUserId(@Param("userId") String userId);

    InsertShowBean selectDeptByUserId(@Param("userId") String userId);

    /**
     * 批量删除该用户相关的表对应的记录
     *
     * @param usersStatusBean
     * @return
     */
    void deleteUsersByUserId(UsersStatusBean usersStatusBean);

    /**
     * 将删除的用户信息存入用户历史表
     *
     * @param list
     * @return
     */
    void batchInsertUserInfo(@Param("list") List<AuthUserInfoEntity> list);

    /**
     * 根据部门id查询该部门所有的用户
     *
     * @param departmentId
     * @return .
     */
    List<AuthUserInfoEntity> getUsersByDepartmentId(String departmentId);

    /**
     * 根据id更新是否首次登陆标志位
     */
    void updateUserById(@Param("id") String id);

    void forbidUsersInfo(@Param("user") AuthUserInfoEntity userInfoEntity);

    /**
     * 获取正在使用的调用方名称列表
     *
     * @return .
     */
    List<String> getCallerNameList();

    /**
     * 根据调用方名称获取用户ID
     *
     * @param callerName
     * @return
     */
    @Select("select user_id from auth_user_info where caller_name=#{callerName}")
    String getUserIdByCallerName(@Param("callerName") String callerName);

    /**
     * 根据部门Id列表查询对应的用户Id列表
     *
     * @param ids
     * @return
     */
    List<String> selectUserIdsByDeptList(@Param("ids") List<String> ids);

    /**
     * @Description: 查询用户级别
     * @Param:
     * @Author: l17503
     * @Date: 2020/1/22 14:00
     */
    String selectUserLevelByUserId(String userId);
}