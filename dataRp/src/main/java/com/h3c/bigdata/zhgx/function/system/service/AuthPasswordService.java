package com.h3c.bigdata.zhgx.function.system.service;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.model.PsdIniSynBean;
import com.h3c.bigdata.zhgx.function.system.model.PsdSynBean;
import com.h3c.bigdata.zhgx.function.system.model.UserPwdBean;
import org.springframework.stereotype.Component;


public interface AuthPasswordService {

    /**
     * 管理员将用户密码重置为初始密码，只对密码表进行操作
     * @param  passwordEntity
     * @throws
     */
    ApiResult<?> updateUserInitialPassword(AuthPasswordEntity passwordEntity,String token);

    /**
     * 用户个人修改密码
     * @param  userPwdBean
     * @throws
     */
    ApiResult<?> updatePswByUser(UserPwdBean userPwdBean,String token);

    ApiResult<?> checkUserPassword(String userId, String password);

    ApiResult<?> queryPlainPasswordByUserId(String userId);

    /**
     * 同步初始密码给研发系统
     * @param  passwordEntity
     * @throws
     */
    PsdIniSynBean updateUserInitialPasswordSynToDC(AuthPasswordEntity passwordEntity);

    /**
     * 用户个人修改密码，同步给研发系统
     * @param  userPwdBean
     * @throws
     */
    PsdSynBean updatePswByUserSynToDC(UserPwdBean userPwdBean);
}
