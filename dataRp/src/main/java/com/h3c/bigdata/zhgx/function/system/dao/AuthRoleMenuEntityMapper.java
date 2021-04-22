package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleMenuEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRoleMenuEntityMapper extends BaseMapper<AuthRoleMenuEntity> {

    void batchInsertRoleMenu(@Param("list") List<AuthRoleMenuEntity> list);

    void deleteRoleMenuByRoleIds(@Param("list") List<AuthRoleInfoEntity> list);

    void deleteRoleMenuByMenuIds(@Param("list") List<AuthMenuInfoEntity> list);

    void updateFunctionByRoleIdAndMenuId(@Param("update") AuthRoleMenuEntity authRoleMenuEntity);

}