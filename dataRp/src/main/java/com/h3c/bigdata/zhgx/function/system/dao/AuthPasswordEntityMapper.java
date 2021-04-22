package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;
import org.springframework.stereotype.Repository;

@Repository

public interface AuthPasswordEntityMapper extends BaseMapper<AuthPasswordEntity> {
    /**
     * 批量删除该用户对应的密码表的记录
     *
     * @param usersStatusBean
     * @return
     */
    void deletePasswordByUserId(UsersStatusBean usersStatusBean);
}

