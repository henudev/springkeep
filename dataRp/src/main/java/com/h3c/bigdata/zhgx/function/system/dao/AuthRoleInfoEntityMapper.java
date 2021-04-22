package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRoleInfoEntityMapper extends BaseMapper<AuthRoleInfoEntity> {

    /**
     * 查询角色列表
     * */
    List<AuthRoleInfoEntity> queryAuthRoleInfoEntityList(AuthRoleInfoEntity authRoleInfoEntity);

    void deleteRolesById(@Param("list") List<AuthRoleInfoEntity> list);

    /**
     * 根据用户id查询角色列表
     */
    List<AuthRoleInfoEntity> queryAuthRoleListByUserId(String userId);

    /**
     * 根据roleName查询角色id信息
     */
    AuthRoleInfoEntity queryRoleInfoByName(@Param("roleName") String roleName);

    /**
     * @Description: 依据roleKey查询角色列表
     * @Author: l17503
     * @Date: 2019/7/12 10:18
     */
    List<AuthRoleInfoEntity> queryByRoleKey(String roleKey);

    /**
     * @Description: 依据roleKey查询角色实体
     * @Author: l17503
     * @Date: 2019/7/12 10:18
     */
    String queryRoleByRoleKey(String roleKey);

    /**
     * @Description: 为流程节点查询角色列表；
     * @Description: 流程节点中角色的待选列表只由部门决定，部门下的用户所关联的角色的并集，即为流程节点中角色的待选列表；
     * @Description: 审批时，当走到当前角色审批节点时，并非由当前角色下的所有用户来审批，而是由当前角色下的所有用户同当前部门下的所有用户求交集的用户来审批。
     * @Param:
     * @Author: l17503
     * @Date: 2020/1/17 10:43
     */
    List<AuthRoleInfoEntity> queryRoleForApprove(String departmentId);
}