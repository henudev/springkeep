package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithMenuListBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: zhgx
 * @description: 菜单全字段和角色主键
 * @author: h17338
 * @create: 2018-08-02 17:08
 **/
@Repository
public interface RoleWithMenuListBeanMapper extends BaseMapper<RoleWithMenuListBean> {

    /**
     * 根据角色id查询菜单列表
     */
    List<RoleWithMenuListBean> queryMenuListByRoleId(RoleWithMenuListBean roleWithMenuListBean);
}
