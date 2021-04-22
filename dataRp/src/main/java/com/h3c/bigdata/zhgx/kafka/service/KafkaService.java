package com.h3c.bigdata.zhgx.kafka.service;

import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;

public interface KafkaService {


    /**
     * @param type               操作类型  create update delete
     * @param sysUser
     * @param authPasswordEntity
     * @return
     * @Description: 将用户信息同步至中台
     * @Author: w15112
     * @Date: 2019/7/18 16:12
     */
    public String syncUserInfo(String type, AuthUserInfoEntity sysUser, AuthPasswordEntity authPasswordEntity);
}
