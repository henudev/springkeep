package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.*;
import com.h3c.bigdata.zhgx.function.system.dao.AuthPasswordEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.model.PsdIniSynBean;
import com.h3c.bigdata.zhgx.function.system.model.PsdSynBean;
import com.h3c.bigdata.zhgx.function.system.model.UserPwdBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthPasswordService;
import com.h3c.bigdata.zhgx.kafka.service.KafkaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: h17338
 * @Description: 密码业务类实现
 * @Date: 2018/7/30
 */

@Service
@Transactional
public class AuthPasswordServiceImpl implements AuthPasswordService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthPasswordServiceImpl.class);

    @Autowired
    private AuthPasswordEntityMapper authPasswordEntityMapper;

    @Autowired
    private KafkaService kafkaService;

    @Value("${pass.word.rule}")
    private  String passwordRule;


    /**
     * 密文密码生成.
     * @param  password,  userId
     * @return 账号，生成的盐值，和加密后的密码
     */
    public Map<String,Object> encryptPassword(String password, String userId, String salt) {
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String algorithmName = "SHA-256";
        int hashIterations = 2;
        if(StringUtil.isNull(salt) || salt.equals("")) {
            salt = randomNumberGenerator.nextBytes().toHex();
        }
        String passwordNew = new SimpleHash(algorithmName, password,
                ByteSource.Util.bytes(userId + salt), hashIterations).toHex();
        Map<String,Object> map  = new HashMap<String,Object>();
        map.put("password", passwordNew);
        map.put("userId", userId);
        map.put("salt", salt);
        return map;
    }

    /**
     * 密码策略校验.
     * @param  newPassword
     * @return 密码策略校验结果
     * @description 密码策略暂时写死，后期考虑通过系统参数界面配置化
     */
    public ApiResult<Object> getPasswordPolicy(String newPassword){
        //校验密码策略1：最小长度
        int minLen = 6;
        if(newPassword.length() < minLen) {
            log.info("密码位数不能多于"+minLen+"位！");
            return ApiResult.fail("密码位数不能多于"+minLen+"位！");
        }
        //校验密码策略2：最大长度
        int maxLen = 15;
        if(newPassword.length() > maxLen) {
            log.info("密码位数不能多于"+maxLen+"位！");
            return ApiResult.fail("密码位数不能多于"+maxLen+"位！");
        }
        int lowerCaseFlag = 0;
        int upperCaseFlag = 0;
        int numberFlag = 0;
        int characterFlag = 0;
        for (int i = 0; i < newPassword.length(); i++) {
            char item = newPassword.charAt(i);
            if(item >= 'a'&&item <= 'z') lowerCaseFlag += 1;
            else if(item >= 'A'&&item <= 'Z') upperCaseFlag += 1;
            else if(item >= '0'&&item <= '9') numberFlag += 1;
            else characterFlag += 1;
        }
//        //校验密码策略3：大写字母位数
//        int digitsOfUpperCases = 1;
//        if(upperCaseFlag < digitsOfUpperCases) {
//            log.info("密码大写字母位数不能少于"+digitsOfUpperCases+"位！");
//            return ApiResult.fail("密码大写字母位数不能少于"+digitsOfUpperCases+"位！");
//        }
//        //校验密码策略4：小写字母位数
//        int digitsOfLowerCases = 1;
//        if(lowerCaseFlag < digitsOfLowerCases) {
//            log.info("密码小写位数不能少于"+digitsOfLowerCases+"位！");
//            return ApiResult.fail("密码小写位数不能少于"+digitsOfLowerCases+"位！");
//        }
//        //校验密码策略5：数字位数
//        int digitsOfNumbers = 1;
//        if(numberFlag < digitsOfNumbers) {
//            log.info("密码数字位数不能少于"+digitsOfNumbers+"位！");
//            return ApiResult.fail("密码数字位数不能少于"+digitsOfNumbers+"位！");
//        }
        //校验密码策略6：特殊字符位数
        int digitsOfCharacters = 1;
        if(characterFlag < digitsOfCharacters) {
            log.info("密码特殊字符位数不能少于"+digitsOfCharacters+"位！");
            return ApiResult.fail("密码特殊字符位数不能少于"+digitsOfCharacters+"位！");
        }
        return ApiResult.success("密码策略校验成功！");
    }

    /**
     * 初始化员工密码
     * @param  passwordEntity
     * @param
     * @throws
     */
    public ApiResult<Object> addUserPassword(AuthPasswordEntity passwordEntity) {
        passwordEntity.setId(UUIDUtil.createUUID());
        passwordEntity.setCreateTime(new java.util.Date());
        passwordEntity.setIsModified("0");
        int count = authPasswordEntityMapper.insert(passwordEntity);
        if (count == 0) {
            log.error("员工密码初始化失败！");
            return ApiResult.fail("员工密码初始化失败！");
        }
        return ApiResult.success("密码策略校验成功！");
    }

    /**
     * 管理员将用户密码重置为初始密码，只对密码表进行操作
     * @param  passwordEntity
     * @throws
     */
    @Override
    public ApiResult<Object> updateUserInitialPassword(AuthPasswordEntity passwordEntity,String token) {
        try{
            //校验必传参数
            String userId = passwordEntity.getUserId();
            if(StringUtil.isNull(userId) || "" == userId) {
                return ApiResult.fail("用户账号不能为空！");
            }
            //验证用户有且只有一条密码
            String password = iniPassword(userId);
            AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
            authPasswordEntity.setUserId(userId);
            List<AuthPasswordEntity> userPsdCountEntity= authPasswordEntityMapper.select(authPasswordEntity);
            if(null != userPsdCountEntity && 0 == userPsdCountEntity.size()) {
                return ApiResult.fail("用户密码数据不存在，无法更新为初始密码！");
            }
            if(null != userPsdCountEntity && userPsdCountEntity.size()>1) {
                return ApiResult.fail("用户密码数据有多条，需处理脏数据！");
            }

            //密码生成规则盐+哈希
            String salt = RandomStringUtils.randomAlphanumeric(20);
            authPasswordEntity.setPassowrd(new Sha256Hash(password, salt).toHex());
            authPasswordEntity.setSalt(salt);
            authPasswordEntity.setIsModified("0");
            authPasswordEntity.setUpdateTime(new java.util.Date());
            authPasswordEntity.setCreateTime(userPsdCountEntity.get(0).getCreateTime());
            authPasswordEntity.setId(userPsdCountEntity.get(0).getId());
            authPasswordEntityMapper.updateByPrimaryKey(authPasswordEntity);

            //重置密码后 登录用户强制退出 必须重新登录
            LoginUtil.removeCache(passwordEntity.getUserId());

            return ApiResult.success("重置密码成功！密码为:用户名"+passwordRule);
        } catch (Exception e){
            log.error("密码初始化失败!",e);
            return ApiResult.fail("密码初始化失败！");
        }
    }

    /**
     * @Description:
     * @Param:
     * @UpdateContent: 添加密码解密
     * @Updater: l17503
     * @UpdateTime: 2019/11/11 19:43
     */
    @Override
    public ApiResult<?> updatePswByUser(UserPwdBean userPwdBean,String token) {
        try{
            String userId = userPwdBean.getUserId();
            String curPassword = EncryptionUtil.aesDecrypt(userPwdBean.getCurPassword());
            String newPassword = EncryptionUtil.aesDecrypt(userPwdBean.getNewPassword());
            String newPasswordAgain = EncryptionUtil.aesDecrypt(userPwdBean.getNewPasswordAgain());
            if(StringUtil.isEmpty(userId)) {
                return ApiResult.fail("未获得当前用户账号！");
            }
            if(StringUtil.isEmpty(curPassword)) {
                return ApiResult.fail("请输入原密码！");
            }
            if(StringUtils.isEmpty(newPassword)) {
                return ApiResult.fail("请输入新密码！");
            }
            if(StringUtil.isEmpty(newPasswordAgain)) {
                return ApiResult.fail("请确认新密码！");
            }
            if(newPassword.length()<8||newPasswordAgain.length()<8) {
                return ApiResult.fail("密码长度小于8位");
            }
            //校验当前密码和确认密码是否相同
            if(curPassword.equals(newPassword)) {
                return ApiResult.fail("新密码不能与原密码相同！");
            }
            //校验当前密码和确认密码是否相同
            if(!newPasswordAgain.equals(newPassword)) {
                return ApiResult.fail("新密码与确认密码不同！");
            }

            //校验用户账号和当前密码有效性
            AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
            authPasswordEntity.setUserId(userId);
            List<AuthPasswordEntity> userPsdCountEntity= authPasswordEntityMapper.select(authPasswordEntity);
            if(null == userPsdCountEntity || userPsdCountEntity.isEmpty()) {
                return ApiResult.fail("用户密码数据不存在，无法更新密码！");
            }else if(userPsdCountEntity.size()>1) {
                return ApiResult.fail("用户密码数据有多条，需处理脏数据！");
            }
            authPasswordEntity = userPsdCountEntity.get(0);
            //当前密码真实性校验
            String pswDB = authPasswordEntity.getPassowrd();
            String saltDb = authPasswordEntity.getSalt();
            if(!pswDB.equals(new Sha256Hash(curPassword, saltDb).toHex())) {
                return ApiResult.fail("原密码输入错误！");
            }
            //新密码密码策略校验
            ApiResult apiResultForPolicy = getPasswordPolicy(newPassword);
            if(apiResultForPolicy.getCode().equals("0")) {
                return ApiResult.fail(apiResultForPolicy.getMessage());
            }

            //密码生成规则盐+哈希
            String salt = RandomStringUtils.randomAlphanumeric(20);
            authPasswordEntity.setSalt(salt);
            authPasswordEntity.setPassowrd(new Sha256Hash(newPassword, salt).toHex());
            authPasswordEntity.setIsModified("1");
            authPasswordEntity.setUpdateTime(new java.util.Date());

            authPasswordEntityMapper.updateByPrimaryKey(authPasswordEntity);

            //重置密码后 登录用户强制退出 必须重新登录
            LoginUtil.removeCache(userId);

            return ApiResult.success("密码修改成功！");
        } catch (Exception e){
            log.error("密码修改失败!",e);
            return ApiResult.fail("密码修改失败！");
        }
    }

    @Override
    public ApiResult<?> checkUserPassword(String userId, String password) {
        //校验用户账号和当前密码有效性
        AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
        authPasswordEntity.setUserId(userId);
        List<AuthPasswordEntity> userPsdCountEntity= authPasswordEntityMapper.select(authPasswordEntity);
        if(null != userPsdCountEntity && 0 == userPsdCountEntity.size()) {
            return ApiResult.fail("用户密码数据不存在！");
        }
        if(null != userPsdCountEntity && userPsdCountEntity.size()>1) {
            return ApiResult.fail("用户密码数据有多条，需处理脏数据！");
        }
        //当前密码真实性校验
        String pswDB = userPsdCountEntity.get(0).getPassowrd();
        String saltDB = userPsdCountEntity.get(0).getSalt();
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        if(!encoder.matches(password, pswDB)) {
            return ApiResult.fail("用户密码校验失败！");
        }
        return ApiResult.success("用户密码校验成功！");
    }

    @Override
    public ApiResult<?> queryPlainPasswordByUserId(String userId) {
        AuthPasswordEntity psdEntity = new AuthPasswordEntity();
        psdEntity.setUserId(userId);
        List<AuthPasswordEntity> resultList = authPasswordEntityMapper.select(psdEntity);
        if(null == resultList ||resultList.isEmpty() || resultList.size() >1) {
            ApiResult.fail("该用户密码数据异常！");
        }
        String desPassword = resultList.get(0).getDesPassword();
        if (StringUtil.isNull(desPassword) || "".equals(desPassword)) {
            return ApiResult.fail("该用户无可解密密文！");
        }
        String password = DesUtil.decode(desPassword);
        return ApiResult.success("",password);
    }

    @Override
    public PsdIniSynBean updateUserInitialPasswordSynToDC(AuthPasswordEntity passwordEntity) {
        PsdIniSynBean psdIniSynBean = new PsdIniSynBean();
        psdIniSynBean.setAdminUserName("admin");
        psdIniSynBean.setUsername(passwordEntity.getUserId());
        psdIniSynBean.setNewPassword(iniPassword(passwordEntity.getUserId()));
        psdIniSynBean.setConfirmPassword(iniPassword(passwordEntity.getUserId()));
        return psdIniSynBean;
    }

    @Override
    public PsdSynBean updatePswByUserSynToDC(UserPwdBean userPwdBean) {
        PsdSynBean psdSynBean = new PsdSynBean();
        psdSynBean.setUsername(userPwdBean.getUserId());
        psdSynBean.setOldPassword(userPwdBean.getCurPassword());
        psdSynBean.setNewPassword(userPwdBean.getNewPassword());
        psdSynBean.setConfirmPassword(userPwdBean.getNewPassword());
        return psdSynBean;
    }

    public  String iniPassword(String userId) {
        //TODO 后期放在配置文件中
        String iniPassword = userId+ passwordRule;
        return iniPassword;
    }
}
