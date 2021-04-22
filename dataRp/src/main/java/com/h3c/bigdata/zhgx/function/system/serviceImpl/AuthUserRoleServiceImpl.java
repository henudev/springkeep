package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.exception.ComBaseException;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserRoleEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.model.UserWithUserRoleListModel;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserRoleService;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zhgx
 * @description: 用户角色关联关系业务实现
 * @author: h17338
 * @create: 2018-07-31 15:44
 **/
@Service
@Transactional
public class AuthUserRoleServiceImpl extends BaseService implements AuthUserRoleService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthUserRoleServiceImpl.class);

    @Autowired
    private AuthUserRoleEntityMapper authUserRoleEntityMapper;

    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthDptService authDptService;

    @Value("${zhgx.admin.roleId}")
    private String ADMIN_ROLE_ID;

    @Override
    public ApiResult<?> addUserRoleInfo(List<AuthUserRoleEntity> authUserRoleEntityList) {
        try {
            //生成主键，入表
            for (int i = 0; i < authUserRoleEntityList.size(); i++) {
                //数据重复性校验
                int count = authUserRoleEntityMapper.selectCount(authUserRoleEntityList.get(i));
                if (count > 0) {
                    return ApiResult.fail("用户角色关系已存在！", authUserRoleEntityList.get(i));
                }
                authUserRoleEntityList.get(i).setId(UUIDUtil.createUUID());
            }
            authUserRoleEntityMapper.batchInsertUserRole(authUserRoleEntityList);
            //        authUserRoleEntityMapper.insertList(authUserRoleEntityList);
            return ApiResult.success("新增用户角色信息成功", authUserRoleEntityList);
        } catch (Exception e) {
            log.error("新增用户角色关联关系失败!", e);
            return ApiResult.fail("新增用户角色关联关系失败！");
        }
    }

    @Override
    public ApiResult<?> updateUserRoleInfo(List<AuthUserRoleEntity> authUserRoleEntityList) {
        try {
            //按照用户编码删除用户关联的所有角色
            AuthUserRoleEntity userRoleEntity = new AuthUserRoleEntity();
            userRoleEntity.setUserId(authUserRoleEntityList.get(0).getUserId());
            authUserRoleEntityMapper.delete(userRoleEntity);
            //批量新增
            for (int i = 0; i < authUserRoleEntityList.size(); i++) {
                authUserRoleEntityList.get(i).setId(UUIDUtil.createUUID());
            }
            authUserRoleEntityMapper.batchInsertUserRole(authUserRoleEntityList);
            return ApiResult.success("更新用户角色信息成功", authUserRoleEntityList);
        } catch (Exception e) {
            log.error("变更用户角色关联关系失败!", e);
            return ApiResult.fail("变更用户角色关联关系失败！");
        }
    }

    @Override
    public ApiResult<?> queryUserRoleInfo(int page, int pageSize, String field, String dir, AuthUserRoleEntity authUserRoleEntity) {
        List<AuthUserRoleEntity> authUserRoleEntityList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            authUserRoleEntityList = authUserRoleEntityMapper.select(authUserRoleEntity);
            result = getDataList(authUserRoleEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "用户角色基本信息查询失败!");
        }
        return ApiResult.success("用户角色基本信息查询成功！", result);
    }

    @Override
    public ApiResult<?> addUseWithRoleList(UserWithUserRoleListModel userWithUserRoleListModel,String token) throws ComBaseException {
        try {
            //新增用户基本信息
            AuthUserInfoEntity authUserInfoEntity = userWithUserRoleListModel.getAuthUserInfoEntity();
            ApiResult apiResult = authUserService.addUserInfo(authUserInfoEntity);
            //新增用户失败，返回失败信息，方法结束
            if (apiResult.getCode().equals("0")) {
                return apiResult;
            }
            //角色列表不为空时，新增用户-角色关联关系
            List<AuthUserRoleEntity> authUserRoleEntityList = userWithUserRoleListModel.getAuthUserRoleEntityList();
            if (!authUserRoleEntityList.isEmpty()) {
                AuthUserInfoEntity UserEntity = (AuthUserInfoEntity) apiResult.getData();
                String userId = UserEntity.getUserId();
                for (int i = 0; i < authUserRoleEntityList.size(); i++) {
                    //将生成的用户账号与角色绑定
                    authUserRoleEntityList.get(i).setUserId(userId);
                }
                this.addUserRoleInfo(authUserRoleEntityList);
            }

            return ApiResult.success("新增用户成功!", userWithUserRoleListModel);
        } catch (Exception e) {
            log.error("新增用户失败！", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResult.fail("新增用户失败！");
        }
    }

    @Override
    public ApiResult<?> updateUseWithRoleList(UserWithUserRoleListModel userWithUserRoleListModel) {
        try {
            //更新用户基本信息
            AuthUserInfoEntity authUserInfoEntity = userWithUserRoleListModel.getAuthUserInfoEntity();
            ApiResult apiResult = authUserService.updateUserInfo(authUserInfoEntity);
            //更新用户失败，返回失败信息，方法结束
            if (apiResult.getCode().equals("0")) {
                return apiResult;
            }
            //更新用户角色关联信息
            List<AuthUserRoleEntity> authUserRoleEntityList = userWithUserRoleListModel.getAuthUserRoleEntityList();
            if (!authUserRoleEntityList.isEmpty()) {
                this.updateUserRoleInfo(authUserRoleEntityList);
            } else {
                //如果账号-角色列空，只做删除操作
                AuthUserRoleEntity userRoleEntity = new AuthUserRoleEntity();
                userRoleEntity.setUserId(authUserInfoEntity.getUserId());
                authUserRoleEntityMapper.delete(userRoleEntity);
            }

            String userId = authUserInfoEntity.getUserId();

            LoginUtil.removeCache(userId);

            return ApiResult.success("编辑用户成功", userWithUserRoleListModel);
        } catch (Exception e) {
            log.error("编辑用户失败!", e);
            return ApiResult.fail("编辑用户失败！");
        }
    }

    /**
     * 查询用户时返回角色关联关系信息（用户、用户角色、角色三表关联查询）
     *
     * @param
     * @return
     */
    @Override
    public ApiResult<?> queryUseWithRoleList(int page, int pageSize, String field, String dir, UserWithRoleModel userWithRoleModel, String userId) {
        List<UserWithRoleModel> userWithRoleModelList;
        PageResult result;
        Map<String, Object> map = new HashMap<>(2);
        AuthUserInfoEntity authUserInfoEntity = authUserInfoEntityMapper.selectUserByUserId(userId);
        boolean isAdmin = false;
        String user_role_key = "";
        List<AuthRoleInfoEntity> roleIdList = authUserRoleEntityMapper.getUserRoleByUserId(userId);

        //判断用户角色是否是部门管理员
        for (AuthRoleInfoEntity model : roleIdList) {
            user_role_key = model.getRoleKey();
            if ("2".equals(user_role_key)) {
                break;
            }
        }
        //判断用户角色是否是平台开发者
        for (AuthRoleInfoEntity model : roleIdList) {
            String role_key = model.getRoleKey();
            if (ADMIN_ROLE_ID.equals(model.getId()) || "1".equals(model.getRoleKey())) {
                isAdmin = true;
                user_role_key = role_key;
                break;
            }
        }
        UserWithRoleModel model = authUserInfoEntityMapper.selectUserModelByUserId(userId);

        try {

            if (isAdmin) {
                startPage(page, pageSize, field, dir);
                userWithRoleModelList = authUserRoleEntityMapper.queryUserWithRole(userWithRoleModel);
            } else {
                userWithRoleModel.setDepartmentId(model.getDepartmentId());
                List<String> childDeptIds = authDptService.getDepListByRoleKey(model.getDepartmentId(),userId);
                userWithRoleModel.setChildDeptId(childDeptIds);
                startPage(page, pageSize, field, dir);
                userWithRoleModelList = authUserRoleEntityMapper.queryUserWithRoleNoAdmin(userWithRoleModel);
            }
//            for (UserWithRoleModel model : userWithRoleModelList) {
//                RcSysDepartmentEntity rcSysDepartmentEntity = slaverAuthDepartmentMapper.
//                        selectByPrimaryKey(model.getDepartmentId());
//                if (null != rcSysDepartmentEntity) {
//                    model.setDepartmentName(rcSysDepartmentEntity.getDepartmentName());
//                }
//            }
            result = getDataList(userWithRoleModelList);

            map.put("dataList", result);
            map.put("user_role_key", user_role_key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "查询用户返回角色基本信息查询失败!");
        }
        return ApiResult.success("查询用户返回角色基本信息查询成功！", map);
    }

    @Override
    public ApiResult<?> queryUseWithRoleListForPerson(int page, int pageSize, String field, String dir, UserWithRoleModel userWithRoleModel) {
        List<UserWithRoleModel> userWithRoleModelList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            userWithRoleModelList = authUserRoleEntityMapper.queryUserWithRoleForPerson(userWithRoleModel);


            result = getDataList(userWithRoleModelList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "查询用户返回角色基本信息查询失败!");
        }
        return ApiResult.success("查询用户返回角色基本信息查询成功！", result);
    }

    @Override
    public ApiResult<?>  checkCallerNameOrPhoneIsUsed(String callerOrPhone,String type) {
        try {
            if(StringUtils.isBlank(callerOrPhone)){
                return ApiResult.success("校验调用方成功",true);
            }
            AuthUserInfoEntity authUserInfoEntity=new AuthUserInfoEntity();

            //判断传入的参数是调用方还是手机号
            if("0".equals(type)){
                authUserInfoEntity.setCallerName(callerOrPhone);
            }else{
                authUserInfoEntity.setPhoneNumber(callerOrPhone);
            }

            int count = authUserInfoEntityMapper.selectCount(authUserInfoEntity);
            if (count==0){
                return ApiResult.success("校验调用方成功",true);
            }
            return ApiResult.success("校验调用方成功",false);
        } catch (Exception e) {
            log.error("校验调用方失败!", e);
            return ApiResult.fail("校验调用方失败！");
        }
    }

}
