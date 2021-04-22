package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.exception.ComErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.system.dao.*;
import com.h3c.bigdata.zhgx.function.system.entity.*;
import com.h3c.bigdata.zhgx.function.system.model.*;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserService;
import com.h3c.bigdata.zhgx.kafka.service.KafkaService;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Service
@Transactional
public class AuthUserServiceImpl extends BaseService implements AuthUserService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthUserServiceImpl.class);

    @Autowired
    private AuthPasswordServiceImpl authPasswordServiceImpl;

    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;

    @Autowired
    private AuthUserRoleEntityMapper authUserRoleEntityMapper;

    @Autowired
    private AuthPasswordEntityMapper authPasswordEntityMapper;


    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuServiceImpl;
    @Autowired
    private AuthPasswordServiceImpl authPasswordService;

    /**
     * @Description:
     * @Param:
     * @Updater: l17503
     * @UpdateTime: 2019/7/30 11:23(去除手机号有效性校验)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> addUserInfo(AuthUserInfoEntity authUserInfoEntity) {
        try {
            //校验必传项
            String dptId = authUserInfoEntity.getDepartmentId();
            if (StringUtil.isNull(dptId) || dptId.equals("")) {
                return ApiResult.fail("部门编码不能为空！");
            }
            String userName = authUserInfoEntity.getUserName();
            if (StringUtil.isNull(userName) || userName.equals("")) {
                return ApiResult.fail("用户姓名不能为空！");
            }
            //手机号、证件号码有效性校验
            /*String phoneNumber = authUserInfoEntity.getPhoneNumber();
            if (!StringUtil.isNull(phoneNumber) && phoneNumber != "" && !isPhone(phoneNumber)) {
                return ApiResult.fail("请输入正确的手机号！");
            }*/
            String identityNumber = authUserInfoEntity.getIdentityNumber();
            if (!StringUtil.isNull(identityNumber) && identityNumber != "" && !isIdentityNumber(identityNumber)) {
                return ApiResult.fail("请输入正确的身份证号！");
            }
            //userId有效性校验1.英文或数字或组合 2.6-25位
            String userId = authUserInfoEntity.getUserId();
            if (StringUtil.isEmpty(userId) || !isUserId(userId)) {
                return ApiResult.fail("请输入正确的用户名！");
            }


            //根据用户名称生成用户拼音
            String userNamePinyin = null;
            String password = null;

            try {
                userNamePinyin = toHanyuPinyin(userName);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

            //生成用户初始密码
            password = authPasswordService.iniPassword(userId);
            //生成密码，插入密码表
            AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
            authPasswordEntity.setUserId(userId);
            //密码生成规则盐+哈希
            String salt = RandomStringUtils.randomAlphanumeric(20);
            authPasswordEntity.setPassowrd(new Sha256Hash(password, salt).toHex());
            authPasswordEntity.setSalt(salt);
            authPasswordServiceImpl.addUserPassword(authPasswordEntity);
            //插入用户表
            authUserInfoEntity.setId(UUIDUtil.createUUID());
            authUserInfoEntity.setUserId(userId);
            authUserInfoEntity.setUserNamePinyin(userNamePinyin);
            authUserInfoEntity.setStatus("0");
            authUserInfoEntity.setFirstLoginFlag("0");
            authUserInfoEntity.setCreateTime(new Date());
            authUserInfoEntityMapper.insert(authUserInfoEntity);
            //返回信息
            return ApiResult.success("新增用户信息成功", authUserInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "新增用户信息失败!");
        }
    }

    /**
     * @Description:
     * @Param:
     * @Updater: l17503
     * @UpdateTime: 2019/7/30 11:22（去除手机号有效性校验）
     */
    @Override
    public ApiResult<?> updateUserInfo(AuthUserInfoEntity authUserInfoEntity) {
        try {
            //手机号、证件号码有效性校验
            /*String phoneNumber = authUserInfoEntity.getPhoneNumber();
            if (phoneNumber != null && phoneNumber != "" && !isPhone(phoneNumber)) {
                return ApiResult.fail("请输入正确的手机号！");
            }*/
            //校验部门编码是否存在且有效
            String dptId = authUserInfoEntity.getDepartmentId();

            AuthUserInfoEntity userInfoEntity4Update = new AuthUserInfoEntity();
            userInfoEntity4Update.setId(authUserInfoEntity.getId());
            userInfoEntity4Update = authUserInfoEntityMapper.selectOne(userInfoEntity4Update);
            userInfoEntity4Update.setUserName(authUserInfoEntity.getUserName());
            userInfoEntity4Update.setSex(authUserInfoEntity.getSex());
            userInfoEntity4Update.setAge(authUserInfoEntity.getAge());
            userInfoEntity4Update.setPhoneNumber(authUserInfoEntity.getPhoneNumber());
            userInfoEntity4Update.setEmailAddress(authUserInfoEntity.getEmailAddress());
            userInfoEntity4Update.setAvatar(authUserInfoEntity.getAvatar());
            userInfoEntity4Update.setIdentityNumber(authUserInfoEntity.getIdentityNumber());
            userInfoEntity4Update.setUpdateTime(new Date());
            userInfoEntity4Update.setCallerName(authUserInfoEntity.getCallerName());

            if (null != dptId) {
                userInfoEntity4Update.setDepartmentId(dptId);
            }
            authUserInfoEntityMapper.updateByPrimaryKey(userInfoEntity4Update);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "用户基本信息更新失败!");
        }
        return ApiResult.success("用户基本信息更新成功！");
    }

    @Override
    public ApiResult<?> queryUserInfo(int page, int pageSize, String field, String dir, AuthUserInfoEntity authUserInfoEntity) {
        List<AuthUserInfoEntity> authUserInfoEntityList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            authUserInfoEntityList = authUserInfoEntityMapper.select(authUserInfoEntity);
            result = getDataList(authUserInfoEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "用户基本信息查询失败!");
        }
        return ApiResult.success("用户基本信息查询成功！", result);
    }

    @Override
    public String deleteUserCheck(String userId) {
        String roleName = null;
        AuthUserInfoEntity authUserInfoEntity = authUserInfoEntityMapper.selectUserByUserId(userId);
        //查询出该用户的所有角色
        List<AuthRoleInfoEntity> authRoleInfoEntity = authUserRoleEntityMapper.getUserRoleByUserId(userId);

        return roleName;
    }

    @Override
    public ApiResult<?> deleteUser(UsersStatusBean usersStatusBean) {
        List<AuthUserInfoEntity> authUserInfoEntityList = new ArrayList<>();
        try {
            for (AuthUserInfoEntity entity : usersStatusBean.getUsers()) {
                //删除用户时，只有用户为正常状态的才进行校验
                if(usersStatusBean.getStatus().equals("0")){
                    String roleName = deleteUserCheck(entity.getUserId());
                    if(roleName!=null){
                        return ApiResult.fail("该用户所属角色["+roleName+"]在审批流程中被使用，不可删除！");
                    }
                }

                //根据user_id获取对应的user_info信息
                AuthUserInfoEntity authUserInfoEntity = authUserInfoEntityMapper.selectUserByUserId(entity.getUserId());
                authUserInfoEntity.setId(UUIDUtil.getUUID());
                authUserInfoEntityList.add(authUserInfoEntity);
                //删除登录信息
                LoginUtil.removeCache(entity.getUserId());

            }

            //删除角色信息
            authUserRoleEntityMapper.deleteUserRoleByUserId(usersStatusBean);

            //批量删除该用户相关的表对应的记录
            authUserInfoEntityMapper.deleteUsersByUserId(usersStatusBean);
            //删除用户的密码记录
            authPasswordEntityMapper.deletePasswordByUserId(usersStatusBean);

            //将删除的用户信息存入用户历史表
            authUserInfoEntityMapper.batchInsertUserInfo(authUserInfoEntityList);
            return ApiResult.success("删除成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "删除用户失败!");
        }
    }

    @Override
    public ApiResult<?> updateUsersStatus(UsersStatusBean usersStatusBean) {
        //校验状态字段有效性
        String status = usersStatusBean.getStatus();
        List<AuthUserInfoEntity> users = usersStatusBean.getUsers();
        try {
            //批量更新用户状态
            //authUserInfoEntityMapper.updateUsersStatus(usersStatusBean);
            for (AuthUserInfoEntity entity : users){
                //如果禁用用户，则需对用户进行校验，确认用户所属角色下是否在流程中被使用，且该角色下是否仅有该用户
                if(status.equals("1")){
                    String roleName = deleteUserCheck(entity.getUserId());
                    if(roleName!=null){
                        return ApiResult.fail("该用户所属角色["+roleName+"]在审批流程中被使用，不可禁用！");
                    }
                }
                entity.setStatus(status);
                authUserInfoEntityMapper.forbidUsersInfo(entity);
                LoginUtil.removeCache(entity.getUserId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (status.equals("0")) {
                return  ApiResult.success("用户启用失败！");
            } else {
                return  ApiResult.success("用户禁用失败！");
            }
        }
        ApiResult<?> apiResult;

        if (status.equals("0")) {
            apiResult = ApiResult.success("用户启用成功！");
        } else {
            apiResult = ApiResult.success("用户禁用成功！");
        }
        return apiResult;
    }

    @Override
    public ApiResult<?> queryUsersByRoleId(String roleId) {
        return ApiResult.success("success", authUserInfoEntityMapper.selectUsersByRoleId(roleId));
    }

    @Override
    public ApiResult<?> queryLoginUser(String userId) {
        AuthUserInfoEntity authUserInfoEntity = new AuthUserInfoEntity();
        authUserInfoEntity.setUserId(userId);
        AuthPasswordEntity authPasswordEntity = new AuthPasswordEntity();
        authPasswordEntity.setUserId(userId);
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            //用户表基本信息校验
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
            if (null != userResult && "1".equals(userResult.get(0).getStatus())) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100003;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }
            //密码表基本信息校验
            List<AuthPasswordEntity> userPsdCountEntity = authPasswordEntityMapper.select(authPasswordEntity);
            if (null != userPsdCountEntity && 0 == userPsdCountEntity.size()) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100004;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }
            if (null != userPsdCountEntity && userPsdCountEntity.size() > 1) {
                comErrorCode = ComErrorCode.LOGIN_ERROR_100005;
                return ApiResult.fail(comErrorCode.getErrorCode(), comErrorCode.getErrorMsg(), comErrorCode.getMsgDetail());
            }

            //根据userId获取该用户的部门信息、最大权限rolekey
            UserWithRoleModel model = authUserInfoEntityMapper.selectUserModelByUserId(userId);
            AuthRoleInfoEntity entity = authRoleMenuServiceImpl.getMinRoleKey(userId);
            if (null == entity){
                entity = new AuthRoleInfoEntity();
                entity.setRoleKey("3");
            }
            resultMap.put("userName", userResult.get(0).getUserId());
            resultMap.put("status", userResult.get(0).getStatus());
            resultMap.put("id", userResult.get(0).getId());
            resultMap.put("name", userResult.get(0).getUserName());
            resultMap.put("avatar", userResult.get(0).getAvatar());
            resultMap.put("firstLoginFlag", userResult.get(0).getFirstLoginFlag());
            resultMap.put("roleKey", entity.getRoleKey());
            if(null != model){
                resultMap.put("departmentId", model.getDepartmentId());
                resultMap.put("departmentName", model.getDepartmentName());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "用户基本信息校验失败!");
        }
        return ApiResult.success("用户基本信息校验成功！", resultMap);
    }


    /**
     * 将文字转为汉语拼音
     *
     * @param chineseLanguage 要转成拼音的中文
     */
    public static String toHanyuPinyin(String chineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        String hanyu = chineseLanguage;
        if (hanyu.contains("灜"))
            hanyu = hanyu.replaceAll("灜", "瀛");
        char[] cl_chars = hanyu.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (int i = 0; i < cl_chars.length; i++) {
            if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
                hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
            } else {// 如果字符不是中文,则不转换
                hanyupinyin += Character.toString(cl_chars[i]);
            }
        }
        return hanyupinyin;
    }

    /**
     * 生成userId（暂时没用）
     *
     * @param userId
     * @description 查询userId最大值，并加1生成新userId
     */
    public static String createNewUserId(String userId, String userNamePinyin) {
        String idNum = "";
        for (int i = userId.length() - 1; i >= 0; i--) {
            if (userId.charAt(i) >= 48 && userId.charAt(i) <= 57) {
                idNum += userId.charAt(i);
            }
        }
        if (idNum == "") {
            idNum = "0";
        }
        int idNumInt = Integer.parseInt(new StringBuffer(idNum).reverse().toString()) + 1;
        userId = userNamePinyin + String.valueOf(idNumInt);
        return userId;
    }

    public static boolean isPhone(String phone) {
        //三大运营商号段
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return false;
        }
        return true;
    }

    public static boolean isIdentityNumber(String identityNumber) {
        String regex = "^\\d+$";
        if (identityNumber.length() != 18) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(identityNumber);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return false;
        }
        return true;
    }

    //只能输入6-25位的英文或者数字
    public static boolean isUserId(String userId) {
        String regex = "[0-9a-zA-Z]{2,25}+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(userId);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return false;
        }
        return true;
    }

    @Override
    public ApiResult<?> getPersonsByDeptId(String deptId) {
        return ApiResult.success("查询该部门下用户列表成功", authUserInfoEntityMapper.getUsersByDepartmentId(deptId));
    }

    /**
     * 查询某用户所在部门的所有人员
     *
     * @param userId
     * @return .
     */
    @Override
    public ApiResult<?> getPersonsByUserId(String userId) {
        List<AuthUserInfoEntity> users = new ArrayList<>();
        try {
            AuthUserInfoEntity authUserInfoEntity = authUserInfoEntityMapper.selectUserByUserId(userId);
            if (null != authUserInfoEntity) {
                users = authUserInfoEntityMapper.getUsersByDepartmentId(authUserInfoEntity.getDepartmentId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "查询某用户所在部门的所有人员!");
        }
        return ApiResult.success("查询某用户所在部门的所有人员！", users);
    }

    /**
     * 更新用户首次登陆标志位
     *
     * @param id
     * @return
     */
    @Override
    public ApiResult<?> updateUserLoginFlag(String id) {

        try {
            authUserInfoEntityMapper.updateUserById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "更新用户首次登陆标志位失败!");
        }
        return ApiResult.success("更新用户首次登陆标志位成功！");
    }
}
