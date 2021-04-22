package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleMenuEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserRoleEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleMenuEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;
import com.h3c.bigdata.zhgx.function.system.service.AuthRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class AuthRoleServiceImpl extends BaseService implements AuthRoleService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthRoleServiceImpl.class);

    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;

    @Autowired
    private AuthRoleMenuEntityMapper authRoleMenuEntityMapper;

    @Autowired
    private AuthUserRoleEntityMapper authUserRoleEntityMapper;
    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuServiceImpl;
    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;

    @Override
    public ApiResult<?> addRoleInfo(AuthRoleInfoEntity authRoleInfoEntity,String userId) {
        try{
            //校验必传参数
            String roleName = authRoleInfoEntity.getRoleName();
            if (StringUtil.isNull(roleName) || roleName.equals("")) {
                return ApiResult.fail("角色名称不能为空！");
            }
            //同名校验
            AuthRoleInfoEntity roleRepeatEntity = new AuthRoleInfoEntity();
            roleRepeatEntity.setRoleName(roleName);
            int repeatCount = authRoleInfoEntityMapper.selectCount(roleRepeatEntity);
            if(repeatCount > 0) {
                return ApiResult.fail("该角色已存在！");
            }
            authRoleInfoEntity.setCreateTime(new Date());
            authRoleInfoEntity.setId(UUIDUtil.createUUID());
            authRoleInfoEntity.setStatus("0");
            //code无分层意义，暂时用uuid规则
            authRoleInfoEntity.setRoleCode(UUIDUtil.createUUID());
            //最高角色为部门管理员时，插入部门ID
            Integer minRoleKey = Integer.valueOf(authRoleMenuServiceImpl.getMinRoleKey(userId).getRoleKey());
            if (Const.DEPARTMENTROLEKEY.equals(minRoleKey) || Const.USERROLEKEY.equals(minRoleKey)){
                AuthUserInfoEntity entity = authUserInfoEntityMapper.selectUserByUserId(userId);
                authRoleInfoEntity.setDepartmentId(entity.getDepartmentId());
            }
            authRoleInfoEntityMapper.insert(authRoleInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "角色基本信息更新失败!");
        }
            return ApiResult.success("新增角色信息成功",authRoleInfoEntity);
    }

    @Override
    public ApiResult<?> updateRoleInfo(AuthRoleInfoEntity authRoleInfoEntity) {
        try {
            authRoleInfoEntity.setUpdateTime(new Date());
            authRoleInfoEntityMapper.updateByPrimaryKey(authRoleInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "角色基本信息更新失败!");
        }
        return ApiResult.success("角色基本信息更新成功！");
    }

    @Override
    public ApiResult<?> queryRoleInfo(int page, int pageSize, String field, String dir, AuthRoleInfoEntity authRoleInfoEntity) {
        List<AuthRoleInfoEntity> authRoleInfoEntityList;
        PageResult result;
        try {
            startPage(page,pageSize,field,dir);
            authRoleInfoEntityList = authRoleInfoEntityMapper.queryAuthRoleInfoEntityList(authRoleInfoEntity);
            result = getDataList(authRoleInfoEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "角色基本信息查询失败!");
        }
        return ApiResult.success("角色基本信息查询成功！",result);
    }

    @Override
    public ApiResult<?> deleteRoleInfo(AuthRoleInfoEntity authRoleInfoEntity) {
        try {
            //校验必传参数
            String roleId = authRoleInfoEntity.getId();
            if (StringUtil.isNull(roleId) || roleId.equals("")) {
                return ApiResult.fail("角色id不能为空！");
            }
            int roleInfoCount = authRoleInfoEntityMapper.selectCount(authRoleInfoEntity);
            if(0 == roleInfoCount) {
                return ApiResult.fail("角色不存在！");
            }
            //删除角色基础信息
            authRoleInfoEntityMapper.delete(authRoleInfoEntity);
            //删除角色权限关联关系
            AuthRoleMenuEntity roleMenuEntity = new AuthRoleMenuEntity();
            roleMenuEntity.setRoleId(roleId);
            authRoleMenuEntityMapper.delete(roleMenuEntity);
            //删除用户角色关联关系
            AuthUserRoleEntity authUserRoleEntity = new AuthUserRoleEntity();
            authUserRoleEntity.setRoleId(roleId);
            authUserRoleEntityMapper.delete(authUserRoleEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "角色基本信息删除失败!");
        }
        return ApiResult.success("角色基本信息删除成功！");
    }

    @Override
    public ApiResult<?> deleteRoles(List<AuthRoleInfoEntity> roles) {
        try{
            authRoleInfoEntityMapper.deleteRolesById(roles);
            authRoleMenuEntityMapper.deleteRoleMenuByRoleIds(roles);
            authUserRoleEntityMapper.deleteUserRoleByRoleId(roles);

            for (AuthRoleInfoEntity authRoleInfoEntity : roles){
                //根据角色id 获取该角色的用户id  并将这些用户进行退出处理
                String roleId = authRoleInfoEntity.getId();
                AuthUserRoleEntity authUserRoleEntity = new AuthUserRoleEntity();
                authUserRoleEntity.setRoleId(roleId);

                List<AuthUserRoleEntity> authUserRoleEntities = authUserRoleEntityMapper.select(authUserRoleEntity);

                for (AuthUserRoleEntity userRoleEntity : authUserRoleEntities){
                    LoginUtil.removeCache(userRoleEntity.getUserId());
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "角色批量删除失败!");
        }
        return ApiResult.success("角色批量删除成功！");
    }
}
