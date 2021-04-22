package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: zhgx
 * @description: 角色和角色权限关联
 * @author: h17338
 * @create: 2018-08-02 17:15
 **/
@Repository
public interface RoleWithRoleMenuBeanMapper extends BaseMapper<RoleWithRoleMenuBean>{

    List<RoleWithRoleMenuBean> queryMenuListByRoleId(RoleWithRoleMenuBean roleWithRoleMenuBean);

    List<RoleWithRoleMenuBean> queryMenuListByRoleIds(@Param("roleIds") List<String> roleIds);
}
