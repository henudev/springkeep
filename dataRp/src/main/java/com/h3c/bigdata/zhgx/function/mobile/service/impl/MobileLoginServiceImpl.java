package com.h3c.bigdata.zhgx.function.mobile.service.impl;


import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.exception.ComErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.tokenSecurity.entity.JwtUser;
import com.h3c.bigdata.zhgx.common.tokenSecurity.util.JwtTokenUtil;
import com.h3c.bigdata.zhgx.common.utils.DesUtil;
import com.h3c.bigdata.zhgx.common.utils.EncryptionUtil;
import com.h3c.bigdata.zhgx.function.mobile.model.LoginDTO;
import com.h3c.bigdata.zhgx.function.mobile.model.LoginResultDTO;
import com.h3c.bigdata.zhgx.function.mobile.service.MobileLoginService;
import com.h3c.bigdata.zhgx.function.system.dao.AuthPasswordEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MobileLoginServiceImpl implements MobileLoginService {

    @Value("${mobile.login.des:false}")
    private boolean passwordDESDecode;

    @Value("${token.invalid:18000}")
    private long tokenInvalid;

    @Value("${login.only}")
    private Boolean loginOnly;
    @Value("${zhgx.admin.password}")
    private String adminPassword;

    private final AuthUserInfoEntityMapper authUserInfoEntityMapper;
    private AuthPasswordEntityMapper authPasswordEntityMapper;
    private JwtTokenUtil jwtTokenUtil;

    public MobileLoginServiceImpl(AuthUserInfoEntityMapper authUserInfoEntityMapper, AuthPasswordEntityMapper authPasswordEntityMapper, JwtTokenUtil jwtTokenUtil) {
        this.authUserInfoEntityMapper = authUserInfoEntityMapper;
        this.authPasswordEntityMapper = authPasswordEntityMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    /**
     * 移动端登录系统
     * loginFlag为false，手机端用户密码登录；loginFlag为true，钉钉内h5免登录
     */
    @Override
    public ApiResult login(LoginDTO loginDTO, Boolean loginFlag) {
        if (StringUtils.isBlank(loginDTO.getUserName()) || StringUtils.isBlank(loginDTO.getPassword())) {
            return ApiResult.fail("用户名密码不能为空");
        }
        String originalPassword = loginDTO.getPassword();
        String passwd;
        String userId = loginDTO.getUserName();

        AuthUserInfoEntity authUserInfoEntity = new AuthUserInfoEntity();
        authUserInfoEntity.setUserId(loginDTO.getUserName());
        List<AuthUserInfoEntity> userResult = authUserInfoEntityMapper.select(authUserInfoEntity);
        ComErrorCode comErrorCode;
        if (null != userResult && 0 == userResult.size()) {
            comErrorCode = ComErrorCode.LOGIN_ERROR_100001;
            return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
        }
        if (null != userResult && userResult.size() > 1) {
            comErrorCode = ComErrorCode.LOGIN_ERROR_100002;
            return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
        }
        AuthUserInfoEntity infoEntity = userResult.get(0);
        if (null != userResult && "1".equals(infoEntity.getStatus())) {
            comErrorCode = ComErrorCode.LOGIN_ERROR_100003;
            return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
        }
        if (loginFlag) {
            passwd = originalPassword;
        } else {
            if (passwordDESDecode) {
                originalPassword = EncryptionUtil.aesDecrypt(originalPassword);
            }
            //密码表基本信息校验
            AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
            authPasswordEntity.setUserId(loginDTO.getUserName());

            List<AuthPasswordEntity> userPsdCountEntity = authPasswordEntityMapper.select(authPasswordEntity);
            if (null != userPsdCountEntity && 0 == userPsdCountEntity.size()) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100004;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }
            if (null != userPsdCountEntity && userPsdCountEntity.size() > 1) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100005;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }
            AuthPasswordEntity passwordEntity = userPsdCountEntity.get(0);
            String password = passwordEntity.getPassowrd();
            String salt = passwordEntity.getSalt();
            String encodePassword = new Sha256Hash(originalPassword, salt).toHex();
            //admin账号登录特殊处理
            if (!password.equals(encodePassword)) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100008;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }

            passwd = authPasswordEntity.getPassowrd();
        }

        authUserInfoEntity = infoEntity;
        JwtUser jwtUser = new JwtUser(userId, passwd, Collections.singleton(null));
        jwtTokenUtil.setEffectiveTime(tokenInvalid);
        String token = jwtTokenUtil.generateToken(jwtUser);
        //根据userId获取该用户的部门信息、最大权限rolekey
        UserWithRoleModel model = authUserInfoEntityMapper.selectUserModelByUserId(userId);
        if (null == model) {
            comErrorCode = ComErrorCode.LOGIN_ERROR_100009;
            return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
        }
        LoginResultDTO loginResultDTO = LoginResultDTO.builder()
                .avatar(authUserInfoEntity.getAvatar())
                .firstLoginFlag(authUserInfoEntity.getFirstLoginFlag())
                .id(authUserInfoEntity.getId())
                .userName(userId)
                .token("Bearer " + token)
                .departmentId(model.getDepartmentId())
                .departmentName(model.getDepartmentName())
                .telephone(infoEntity.getPhoneNumber())
                .email(infoEntity.getEmailAddress())
                .name(infoEntity.getUserName())
                .build();

        List<String> tokenArr = CaCheMapConst.USERTICKETCACHE.get(userId);
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return userId;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };
        if (tokenArr == null || tokenArr.size() == 0) {
            tokenArr = new ArrayList<>();
            tokenArr.add(token);
        } else {
            if (loginOnly) {
                // 将缓存中的token 失效
                String oldToken = tokenArr.get(0);
                // 将待删除token信息存入
                CaCheMapConst.USERLOUGOUTCHAHE.put(oldToken, userDetails.getUsername());
                CaCheMapConst.USERCACHE.invalidate(oldToken);

                tokenArr = new ArrayList<>();
                tokenArr.add(token);
            } else {
                tokenArr.add(token);
            }
        }
        CaCheMapConst.USERTICKETCACHE.put(userDetails.getUsername(), tokenArr);

        CaCheMapConst.USERCACHE.put(token, userDetails);
        return ApiResult.success(loginResultDTO);
    }
}
