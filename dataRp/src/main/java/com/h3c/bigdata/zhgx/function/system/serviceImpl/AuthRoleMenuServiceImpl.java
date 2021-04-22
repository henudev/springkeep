package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.PageUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.system.dao.*;
import com.h3c.bigdata.zhgx.function.system.entity.*;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.RoleWithRoleMenuListModel;
import com.h3c.bigdata.zhgx.function.system.model.UserWithRoleModel;
import com.h3c.bigdata.zhgx.function.system.service.AuthRoleMenuService;
import com.h3c.bigdata.zhgx.function.system.service.AuthRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @program: zhgx
 * @description: 角色权限关联关系实现类
 * @author: h17338
 * @create: 2018-07-31 15:53
 **/
@Service
@Transactional
public class AuthRoleMenuServiceImpl extends BaseService implements AuthRoleMenuService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthRoleMenuServiceImpl.class);

    @Autowired
    private AuthRoleMenuEntityMapper authRoleMenuEntityMapper;

    @Autowired
    private AuthRoleTypeEntityMapper authRoleTypeEntityMapper;

    @Autowired
    RoleWithRoleMenuBeanMapper roleWithRoleMenuBeanMapper;

    @Autowired
    private AuthRoleService authRoleService;

    @Autowired
    private AuthUserRoleEntityMapper authUserRoleEntityMapper;

    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;
    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;
    @Autowired
    private AuthMenuInfoEntityMapper authMenuInfoEntityMapper;

    @Override
    public ApiResult<?> addRoleMenuInfo(List<AuthRoleMenuEntity> authRoleMenuEntityList) {
        try {
            //生成主键，入表
            for (int i = 0; i < authRoleMenuEntityList.size(); i++) {
                authRoleMenuEntityList.get(i).setId(UUIDUtil.createUUID());
            }
            authRoleMenuEntityMapper.batchInsertRoleMenu(authRoleMenuEntityList);
            return ApiResult.success("新增角色权限信息成功", authRoleMenuEntityList);
        } catch (Exception e) {
            log.error("新增角色权限关系失败!", e);
            return ApiResult.fail("新增角色权限关系失败！");
        }
    }

    @Override
    public ApiResult<?> updateRoleMenuInfo(List<AuthRoleMenuEntity> authRoleMenuEntityList) {
        try {
            //批量新增
            for (AuthRoleMenuEntity entity : authRoleMenuEntityList) {
                entity.setId(UUIDUtil.createUUID());
            }
            authRoleMenuEntityMapper.batchInsertRoleMenu(authRoleMenuEntityList);
            return ApiResult.success("变更角色权限信息成功", authRoleMenuEntityList);
        } catch (Exception e) {
            log.error("变更角色权限关系失败!", e);
            return ApiResult.fail("变更角色权限关系失败！");
        }
    }

    @Override
    public ApiResult<?> queryRoleMenuInfo(int page, int pageSize, String field, String dir, AuthRoleMenuEntity authRoleMenuEntity) {
        List<AuthRoleMenuEntity> authRoleMenuEntityList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            authRoleMenuEntityList = authRoleMenuEntityMapper.select(authRoleMenuEntity);
            result = getDataList(authRoleMenuEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "角色权限基本信息查询失败!");
        }
        return ApiResult.success("角色权限基本信息查询成功！", result);
    }

    /**
     * @param
     * @return
     * @throws
     * @Description: 获取角色类型列表方法
     * @Author: w15112
     * @CreateDate: 2018/11/27 14:07
     */
    @Override
    public ApiResult<?> getRoleTypeList(String userId) {

        //获取该用户的最大权限，即role_key最小值
        Integer minRoleKey = Integer.valueOf(getMinRoleKey(userId).getRoleKey());
        List<AuthRoleTypeEntity> list;
        try {
            list = authRoleTypeEntityMapper.selectAll();
            List<AuthRoleTypeEntity> roleList = list.stream()
                    .filter(item -> Integer.valueOf(item.getRoleKey()) > minRoleKey)
                    .collect(toList());
            return ApiResult.success("获取角色类型列表成功", roleList);
        } catch (Exception e) {
            log.error("获取角色类型列表失败!", e);
            return ApiResult.fail("获取角色类型列表失败！");
        }
    }

    /**
     * 获取该用户的最大权限的实体类，即role_key最小值
     *
     * @param userId
     * @return
     */
    public AuthRoleInfoEntity getMinRoleKey(String userId) {
        AuthRoleInfoEntity authRoleInfoEntity = new AuthRoleInfoEntity();
        List<AuthRoleInfoEntity> roleIdList = authUserRoleEntityMapper.getUserRoleByUserId(userId);
        Optional<AuthRoleInfoEntity> entity = roleIdList.stream().
                collect(Collectors.minBy(Comparator.comparing(AuthRoleInfoEntity::getRoleKey)));
        if (null!=userId){
            authRoleInfoEntity =entity.get();
        }
        return authRoleInfoEntity;
    }

    @Override
    public ApiResult<?> addRoleWithMenuList(RoleWithRoleMenuListModel roleWithRoleMenuListModel, String userId) {
        try {
            //新增角色基本信息
            AuthRoleInfoEntity authRoleInfoEntity = roleWithRoleMenuListModel.getAuthRoleInfoEntity();
            ApiResult apiResult = authRoleService.addRoleInfo(authRoleInfoEntity, userId);
            List<AuthRoleMenuEntity> authRoleMenuLists = new ArrayList<>();
            //勾选角色权限
            List<AuthRoleMenuEntity> authRoleMenuEntityList = roleWithRoleMenuListModel.getAuthRoleMenuEntityList();
            if (!authRoleMenuEntityList.isEmpty()) {
                AuthRoleInfoEntity roleEntity = (AuthRoleInfoEntity) apiResult.getData();
                if (apiResult.getCode().equals("0") || roleEntity == null) {
                    return apiResult;
                }
                getNewRoleMenuList(authRoleMenuLists, authRoleMenuEntityList);
                String roleId = roleEntity.getId();
                for (AuthRoleMenuEntity bean : authRoleMenuLists) {
                    //将生成的角色主键与权限绑定
                    bean.setRoleId(roleId);
                }

                this.addRoleMenuInfo(authRoleMenuLists);
            }
            return ApiResult.success("新增角色并勾选权限信息成功", roleWithRoleMenuListModel);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增角色权限关系失败!", e);
            return ApiResult.fail("新增角色权限关系失败！");
        }
    }

    /**
     * 重组authRoleMenuEntityList，添加页面菜单的AuthRoleMenuEntity到authRoleMenuLists中
     *
     * @param authRoleMenuEntityList
     * @param authRoleMenuLists
     * @return
     */
    private void getNewRoleMenuList(List<AuthRoleMenuEntity> authRoleMenuLists, List<AuthRoleMenuEntity> authRoleMenuEntityList) {
        for (AuthRoleMenuEntity entity : authRoleMenuEntityList) {
            List<AuthMenuInfoEntity> pageMenuList = authMenuInfoEntityMapper.
                    queryMenuButtonByMenuCode(entity.getMenuCode());
            for (AuthMenuInfoEntity authMenuInfoEntity : pageMenuList) {
                AuthRoleMenuEntity authRoleMenuEntity = new AuthRoleMenuEntity();
                authRoleMenuEntity.setMenuId(authMenuInfoEntity.getId());
                authRoleMenuLists.add(authRoleMenuEntity);
            }
            authRoleMenuLists.add(entity);
        }
    }

    @Override
    public ApiResult<?> updateRoleWithMenuList(RoleWithRoleMenuListModel roleWithRoleMenuListModel) {
        try {
            List<AuthRoleMenuEntity> authRoleMenuLists = new ArrayList<>();
            //更新角色基本信息
            AuthRoleInfoEntity authRoleInfoEntity = roleWithRoleMenuListModel.getAuthRoleInfoEntity();
            authRoleService.updateRoleInfo(authRoleInfoEntity);
            //更新角色权限关联关系
            List<AuthRoleMenuEntity> authRoleMenuEntityList = roleWithRoleMenuListModel.getAuthRoleMenuEntityList();
            //按照角色编码删除角色权限关联关系
            AuthRoleMenuEntity authRoleMenuEntity = new AuthRoleMenuEntity();
            String roleId = authRoleInfoEntity.getId();
            authRoleMenuEntity.setRoleId(roleId);
            authRoleMenuEntityMapper.delete(authRoleMenuEntity);

            getNewRoleMenuList(authRoleMenuLists, authRoleMenuEntityList);
            for (AuthRoleMenuEntity bean : authRoleMenuLists) {
                //将生成的角色主键与权限绑定
                bean.setRoleId(roleId);
            }
            this.updateRoleMenuInfo(authRoleMenuLists);

            //根据角色id 获取该角色的用户id  并将这些用户进行退出处理
            AuthUserRoleEntity authUserRoleEntity = new AuthUserRoleEntity();
            authUserRoleEntity.setRoleId(roleId);

            List<AuthUserRoleEntity> authUserRoleEntities = authUserRoleEntityMapper.select(authUserRoleEntity);

            for (AuthUserRoleEntity userRoleEntity : authUserRoleEntities) {
                LoginUtil.removeCache(userRoleEntity.getUserId());
            }

            return ApiResult.success("编辑角色并勾选权限信息成功", roleWithRoleMenuListModel);
        } catch (Exception e) {
            log.error("变更角色权限关系失败!", e);
            return ApiResult.fail("变更角色权限关系失败！");
        }
    }

    /**
     * @Description: 为流程节点查询角色列表
     * @Param:
     * @UpdateContent: 流程节点中角色的待选列表只由部门决定，部门下的用户所关联的角色的并集，即为流程节点中角色的待选列表；
     * @UpdateContent: 审批时，当走到当前角色审批节点时，并非由当前角色下的所有用户来审批，而是由当前角色下的所有用户同当前部门下的所有用户求交集的用户来审批。
     * @Updater: l17503
     * @UpdateTime: 2020/1/17 10:54
     */
    public ApiResult<?> queryRoleForApprove(RoleWithRoleMenuBean roleWithRoleMenuBean) {
        try {
            List<AuthRoleInfoEntity> roleWithMenuListBeans = authRoleInfoEntityMapper.queryRoleForApprove(roleWithRoleMenuBean.getDepartmentId());
            return ApiResult.success("查询角色返回权限列表成功！", roleWithMenuListBeans);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "查询角色返回权限列表失败!");
        }
    }


    /**
     * 该方法主要查询出三种情况的用户：
     * 1.普通用户角色但是部门为传入部门id的用户
     * 2.部门管理员角色但是部门为传入部门id的用户
     * 3.超级管理员用户
     */
    public List<UserWithRoleModel> haveUserRole(String deptId){
        //查询出部门管理员角色的详细信息
        List<AuthRoleInfoEntity> authRoleInfoEntities = authRoleInfoEntityMapper.queryByRoleKey("2");
        //查询出普通用户，并过滤出为deptid的普通用户及角色
        List<AuthRoleInfoEntity> commonAuthRole = authRoleInfoEntityMapper.queryByRoleKey("3");
        commonAuthRole = commonAuthRole.stream().filter(item ->item.getDepartmentId()!=null && item.getDepartmentId().equals(deptId)).collect(toList());
        List<String> roleIds = new ArrayList<>();
        for(AuthRoleInfoEntity authRoleInfoEntity:authRoleInfoEntities){
            roleIds.add(authRoleInfoEntity.getId());
        }
        for(AuthRoleInfoEntity authRoleInfoEntity:commonAuthRole){
            roleIds.add(authRoleInfoEntity.getId());
        }
        //查询出角色为部门管理员及普通用户，且部门为deptId的用户及角色
        List<UserWithRoleModel> userWithRoleModels = authUserRoleEntityMapper.queryUserByDepetRole(deptId,roleIds);

        //查询出系统管理员的用户及角色
        List<AuthRoleInfoEntity> systemRoleInfoEntities = authRoleInfoEntityMapper.queryByRoleKey("1");
        roleIds.clear();
        for(AuthRoleInfoEntity authRoleInfoEntity:systemRoleInfoEntities){
            roleIds.add(authRoleInfoEntity.getId());
        }
        List<UserWithRoleModel> systemAdminUser= authUserRoleEntityMapper.queryUserByRoleIds(roleIds);
        if(systemAdminUser!=null){
            for(UserWithRoleModel userWithRoleModel:systemAdminUser){
                userWithRoleModels.add(userWithRoleModel);
            }
        }
        return userWithRoleModels;
    }

    /**
     * 查询角色和已勾选权限列表（用户、用户角色、角色三表关联查询）
     *
     * @param page
     * @param pageSize
     * @param field
     * @param roleWithRoleMenuBean
     * @param userId
     * @return
     */
    @Override
    public ApiResult<?> queryRoleWithMenuList(int page, int pageSize, String field, String dir,
                                              RoleWithRoleMenuBean roleWithRoleMenuBean, String userId) {
        List<RoleWithRoleMenuBean> roleWithRoleMenuBeanList;
        Integer minRoleKey = Integer.valueOf(getMinRoleKey(userId).getRoleKey());
        try {
            roleWithRoleMenuBeanList = roleWithRoleMenuBeanMapper.queryMenuListByRoleId(roleWithRoleMenuBean);
            roleWithRoleMenuBeanList = roleWithRoleMenuBeanList.stream().
                    filter(item -> Integer.valueOf(item.getRoleKey()) > minRoleKey).collect(toList());
            //部门管理员只允许查看本部门下的角色列表
            if (Const.DEPARTMENTROLEKEY.equals(minRoleKey)) {
                AuthUserInfoEntity entity = authUserInfoEntityMapper.selectUserByUserId(userId);
                roleWithRoleMenuBeanList = roleWithRoleMenuBeanList.stream().
                        filter(item -> entity.getDepartmentId().equals(item.getDepartmentId())||(item.getDepartmentId()==null&&item.getRoleKey().equals("3"))).
                        collect(toList());
            }
            PageResult result = getPageList(page, pageSize, roleWithRoleMenuBeanList);
            return ApiResult.success("查询角色返回权限列表成功！", result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "查询角色返回权限列表失败!");
        }
    }



    /**
     * 分页并返回数据，传入pagesize参数，进行分页。返回数据与数据的总数
     *
     * @param page
     * @param pageSize
     * @param roleWithRoleMenuBeanList
     * @return
     */
    private PageResult getPageList(int page, int pageSize, List<RoleWithRoleMenuBean> roleWithRoleMenuBeanList) {
        PageResult res = new PageResult();

        if (roleWithRoleMenuBeanList.size() > 0) {
            PageUtil<RoleWithRoleMenuBean> pag = new PageUtil<>(roleWithRoleMenuBeanList, pageSize);
            pag.setCurrent_page(page);
            res.setData(pag.getCurrentPageData());
            res.setTotal(pag.getTotal_sum());
        }
        return res;
    }

}
