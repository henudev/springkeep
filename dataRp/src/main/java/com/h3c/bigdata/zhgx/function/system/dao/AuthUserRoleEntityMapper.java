package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthUserRoleEntityMapper extends BaseMapper<AuthUserRoleEntity> {

    void batchInsertUserRole(@Param("list") List<AuthUserRoleEntity> list);

    /**
     * 用户-角色一一对应，关联用户、用户角色、角色表查询，用户列表
     * @param userWithRoleModel
     * @return
     */
    List<UserWithRoleModel> queryUserWithRole(UserWithRoleModel userWithRoleModel);

    /**
     * 用户-角色一一对应，关联用户、用户角色、角色表查询，用户列表
     * @param userWithRoleModel
     * @return
     */
    List<UserWithRoleModel> queryUserWithRoleNoAdmin(UserWithRoleModel userWithRoleModel);

    List<UserWithRoleModel> queryUserByDepetRole(@Param("deptId") String deptId, @Param("roleIds") List<String> roleIds);

    List<UserWithRoleModel> queryUserByRoleIds(@Param("roleIds")List<String> roleIds);

    /**
     * 用户-角色一一对应，关联用户、用户角色、角色表查询，个人中心
     * @param userWithRoleModel
     * @return
     */
    List<UserWithRoleModel> queryUserWithRoleForPerson(UserWithRoleModel userWithRoleModel);

    void deleteUserRoleByUserId(UsersStatusBean usersStatusBean);

    void deleteUserRoleByRoleId(List<AuthRoleInfoEntity> roles);

    List<AuthUserRoleEntity> queryUserRoleByRoleId(@Param("roleId") String roleId);

    List<AuthUserRoleEntity> queryUserRoleByRoleName(@Param("roleName") String roleName,@Param("departmentId") String departmentId);

    /**
     * 根据user_id获取角色信息列表
     *
     * @param user_id
     * @return
     * @throws
     */
    List<AuthRoleInfoEntity> getUserRoleByUserId(@Param("user_id") String user_id);
}