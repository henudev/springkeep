package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.security.casrest.CasRestClient;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.PropertyUtil;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.system.dao.AuthMenuInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleMenuEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleMenuEntity;
import com.h3c.bigdata.zhgx.function.system.model.AuthMenuBean;
import com.h3c.bigdata.zhgx.function.system.model.LoginMenuBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthMenuService;
import com.h3c.bigdata.zhgx.function.system.service.AuthPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * @program: zhgx
 * @description: 权限维护实现类
 * @author: h17338
 * @create: 2018-07-31 10:00
 **/
@Service
@Transactional
public class AuthMenuServiceImpl extends BaseService implements AuthMenuService {

    @Value("${zhgx.admin.roleId}")
    private String adminRoleId;

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthMenuServiceImpl.class);

    @Autowired
    private AuthMenuInfoEntityMapper authMenuInfoEntityMapper;

    @Autowired
    private AuthRoleMenuEntityMapper authRoleMenuEntityMapper;

    @Autowired
    private AuthPasswordService authPasswordService;
    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuServiceImpl;


    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;

    @Override
    public ApiResult<?> addMenuInfo(AuthMenuBean authMenuBean, String userId) {
        try {
            AuthMenuInfoEntity authMenuInfoEntity = authMenuBean.getAuthMenuInfoEntity();

            List<AuthMenuInfoEntity> pageList = authMenuBean.getPageList();
            //校验必传参数
            String menuName = authMenuInfoEntity.getMenuName();
            if (StringUtil.isNull(menuName) || menuName.equals("")) {
                return ApiResult.fail("权限名称不能为空！");
            }
            String parentMenuCode = authMenuInfoEntity.getParentMenuCode();
            if (StringUtil.isNull(parentMenuCode) || parentMenuCode.equals("")) {
                return ApiResult.fail("父级权限编码不能为空！");
            }
            String menuType = authMenuInfoEntity.getMenuType();
            if (StringUtil.isNull(menuType) || menuType.equals("")) {
                return ApiResult.fail("权限类型不能为空！");
            }
            //同级下不能有同名权限
            AuthMenuInfoEntity repeatNameEntity = new AuthMenuInfoEntity();
            repeatNameEntity.setParentMenuCode(parentMenuCode);
            repeatNameEntity.setMenuName(menuName);
            int repeatCount = authMenuInfoEntityMapper.selectCount(repeatNameEntity);
            if (repeatCount > 0) {
                return ApiResult.fail("名称重复！");
            }
            //根据父级编码生成新权限编码
            String menuCode = "";
            Map<String, Object> codeQueryMap = new HashMap<>();
            codeQueryMap.put("parentMenuCode", parentMenuCode);
            List<AuthMenuInfoEntity> menuInfoEntityList = authMenuInfoEntityMapper.queryMaxMenuCodeByParentCode(codeQueryMap);
            menuCode = getMenuCode(parentMenuCode, menuInfoEntityList);
            //新增入表
            authMenuInfoEntity.setMenuCode(menuCode);
            authMenuInfoEntity.setId(UUIDUtil.createUUID());
            authMenuInfoEntity.setStatus("0");
            authMenuInfoEntity.setCreateUser(userId);
            authMenuInfoEntity.setCreateTime(new Date());
            authMenuInfoEntityMapper.insert(authMenuInfoEntity);


            //新增入超管和menu_info的关联记录到auth_role_menu表
            AddMenuRole(authMenuInfoEntity);


            //插入页面菜单关联功能菜单信息到数据库.
            if (null != pageList) {
                insertPageMenu(pageList, menuCode, userId);
            }
            return ApiResult.success("新增权限信息成功", authMenuInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "新增权限信息失败!");
        }
    }

    private void AddMenuRole(AuthMenuInfoEntity authMenuInfoEntity) {
        AuthRoleMenuEntity authRoleMenuEntity = new AuthRoleMenuEntity();
        authRoleMenuEntity.setId(UUIDUtil.createUUID());
        authRoleMenuEntity.setMenuId(authMenuInfoEntity.getId());
        //设置角色Id为超管Id
        authRoleMenuEntity.setRoleId(adminRoleId);
        authRoleMenuEntity.setFunctionType(authMenuInfoEntity.getFunctionType());
        authRoleMenuEntityMapper.insert(authRoleMenuEntity);
    }

    @Override
    public ApiResult<?> updateMenuInfo(AuthMenuBean authMenuBean, String userId) {
        try {

            AuthMenuInfoEntity authMenuInfoEntity = authMenuBean.getAuthMenuInfoEntity();

            List<AuthMenuInfoEntity> pageList = authMenuBean.getPageList();

            //校验必传参数
            String menuId = authMenuInfoEntity.getId();
            if (StringUtil.isNull(menuId) || menuId.equals("")) {
                return ApiResult.fail("权限id不能为空！");
            }
            String menuCode = authMenuInfoEntity.getMenuCode();
            if (StringUtil.isNull(menuCode) || menuCode.equals("")) {
                return ApiResult.fail("权限code不能为空！");
            }
            //若修改状态为停用，校验节点下是否有在用权限节点
            if (StringUtil.isNotNull(authMenuInfoEntity.getStatus()) && authMenuInfoEntity.getStatus().equals("1")) {
                AuthMenuInfoEntity menuEntity = new AuthMenuInfoEntity();
                menuEntity.setParentMenuCode(menuCode);
                menuEntity.setStatus("0");
                int childrenMenuCount = authMenuInfoEntityMapper.selectCount(menuEntity);
                if (childrenMenuCount > 0) {
                    return ApiResult.fail("该权限节点下存在启用状态的权限子节点！");
                }
            }
            authMenuInfoEntity.setUpdateTime(new Date());
            authMenuInfoEntity.setUpdateUser(userId);
            authMenuInfoEntityMapper.updateByPrimaryKey(authMenuInfoEntity);

           /* if (Const.FUNCTIONMENUNUM.equals(authMenuInfoEntity.getMenuType())){
                //更新页面菜单.
                updatePageMenu(pageList,menuCode,userId);
            }*/
            //删除原有功能菜单下的页面菜单.
            authMenuInfoEntityMapper.deleteMenuByParentMenuAndType(menuCode);

            if (pageList.size()>0){
                //批量删除角色权限关联表
                authRoleMenuEntityMapper.deleteRoleMenuByMenuIds(pageList);
                //插入页面菜单关联功能菜单到数据库.
                insertPageMenu(pageList, menuCode, userId);
            }

            //更新admin角色
            AuthRoleMenuEntity authRoleMenuEntity = new AuthRoleMenuEntity();
            authRoleMenuEntity.setRoleId(adminRoleId);
            authRoleMenuEntity.setMenuId(authMenuInfoEntity.getId());
            authRoleMenuEntity.setFunctionType(authMenuInfoEntity.getFunctionType());
            authRoleMenuEntityMapper.updateFunctionByRoleIdAndMenuId(authRoleMenuEntity);

            //LoginUtil.removeAllCache();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "权限基本信息更新失败!");
        }
        return ApiResult.success("权限基本信息更新成功！");
    }

    @Override
    public ApiResult<?> queryMenuInfo(int page, int pageSize, String field, String dir, AuthMenuInfoEntity authMenuInfoEntity) {
        List<AuthMenuInfoEntity> authMenuInfoEntityList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            authMenuInfoEntityList = authMenuInfoEntityMapper.queryMenuInfoList(authMenuInfoEntity);
            result = getDataList(authMenuInfoEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "权限基本信息查询失败!");
        }
        return ApiResult.success("权限基本信息查询成功！", result);
    }

    @Override
    public ApiResult<?> queryMenuInfoPinpoint(int page, int pageSize, String field, String dir, AuthMenuInfoEntity authMenuInfoEntity) {
        List<AuthMenuInfoEntity> authMenuInfoEntityList;
        PageResult result;
        try {
            startPage(page, pageSize, field, dir);
            authMenuInfoEntityList = authMenuInfoEntityMapper.select(authMenuInfoEntity);
            result = getDataList(authMenuInfoEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "权限基本信息查询失败!");
        }
        return ApiResult.success("权限基本信息查询成功！", result);
    }

    @Override
    public ApiResult<?> deleteMenuInfo(AuthMenuInfoEntity authMenuInfoEntity) {
        try {
            //校验必传参数
            String menuId = authMenuInfoEntity.getId();
            if (StringUtil.isNull(menuId) || menuId.equals("")) {
                return ApiResult.fail("权限id不能为空！");
            }
            String menuCode = authMenuInfoEntity.getMenuCode();
            if (StringUtil.isNull(menuCode) || menuCode.equals("")) {
                return ApiResult.fail("权限code不能为空！");
            }
            int roleInfoCount = authMenuInfoEntityMapper.selectCount(authMenuInfoEntity);
            if (0 == roleInfoCount) {
                return ApiResult.fail("权限不存在！");
            }
            //校验被删除权限是否存在子节点
            AuthMenuInfoEntity menuEntity = new AuthMenuInfoEntity();
            menuEntity.setParentMenuCode(menuCode);
            int childrenMenuCount = authMenuInfoEntityMapper.selectCount(menuEntity);
            if (childrenMenuCount > 0) {
                return ApiResult.fail("该权限节点下存在未被删除的权限子节点！");
            }
            //删除权限基础信息
            authMenuInfoEntityMapper.delete(authMenuInfoEntity);
            //删除角色权限关联关系
            AuthRoleMenuEntity roleMenuEntity = new AuthRoleMenuEntity();
            roleMenuEntity.setMenuId(menuId);
            authRoleMenuEntityMapper.delete(roleMenuEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.DELETE_DATA_FAILED, "权限基本信息删除失败!");
        }
        return ApiResult.success("权限基本信息删除成功！");
    }

    @Override
    public ApiResult<?> deleteMenus(List<AuthMenuInfoEntity> menus) {
        try {
            //校验权限下是否有未删除权限
            List<AuthMenuInfoEntity> menusCheck = authMenuInfoEntityMapper.queryChildrenMenusByParentId(menus);
            if (!menusCheck.isEmpty()) {
                return ApiResult.fail("删除失败，被删除菜单节点存在未被删除的菜单子节点！");
            }
            AuthMenuInfoEntity entity = authMenuInfoEntityMapper.selectByPrimaryKey(menus.get(0));
            //获取菜单及子菜单列表
            menus = authMenuInfoEntityMapper.queryMeunAndChilden(entity.getId(),entity.getMenuCode());
            //批量删除角色权限关联表
            authRoleMenuEntityMapper.deleteRoleMenuByMenuIds(menus);
            //批量删除菜单表，页面
            authMenuInfoEntityMapper.deleteMenusByParentMenuId(entity.getMenuCode());
            //批量删除菜单表，分类、功能
            authMenuInfoEntityMapper.deleteMenusByMenuIds(menus);

            // LoginUtil.removeAllCache();
            return ApiResult.success("菜单删除成功！");
        } catch (Exception e) {
            return ApiResult.fail("菜单删除失败！");
        }
    }

    @Override
    public ApiResult<?> queryMenuTree(AuthMenuInfoEntity authMenuInfoEntity) {
        List<AuthMenuInfoEntity> result;
        try {
            result = authMenuInfoEntityMapper.queryMenuTree(authMenuInfoEntity);
            removeChildren(result);
            return ApiResult.success("菜单树查询成功！", result);
        } catch (Exception e) {
            return ApiResult.fail("菜单树查询失败！");
        }
    }

    /**
     * 获取父级菜单分类列表
     *
     * @param
     * @return
     */
    @Override
    public ApiResult<?> getMenuCategoryList() {
        List<AuthMenuInfoEntity> result;
        try {
            result = authMenuInfoEntityMapper.getMenuCategoryList();
            return ApiResult.success("获取父级菜单分类列表成功！", result);
        } catch (Exception e) {
            return ApiResult.fail("获取父级菜单分类列表失败！");
        }
    }

    public void removeChildren(List<AuthMenuInfoEntity> list) {
        Iterator<AuthMenuInfoEntity> it = list.iterator();
        while (it.hasNext()) {
            AuthMenuInfoEntity authMenu = it.next();
            boolean res = removeChildren(authMenu, list);
            if (res) {
                it.remove();
            }
        }
    }

    public boolean removeChildren(AuthMenuInfoEntity authMenuInfoEntity, List<AuthMenuInfoEntity> list) {
        for (AuthMenuInfoEntity authMenu : list) {
            if (authMenu == authMenuInfoEntity) {
                continue;
            }
            if (authMenu.getId().equals(authMenuInfoEntity.getId())) {
                return true;
            }
            boolean res = removeChildren(authMenuInfoEntity, authMenu.getChildMenus());
            if (res) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ApiResult<?> queryMenuTreeByUserId(String userId) {
        try {
            List<AuthMenuInfoEntity> menuList = authMenuInfoEntityMapper.queryMenuListByUserId(userId);
            //将查到的菜单列表拼装成树
            AuthMenuInfoEntity menuRoot = authMenuInfoEntityMapper.queryRootMenu();
            List<AuthMenuInfoEntity> menuTreeWithoutRoot = getMenuTreeByList(menuList, menuRoot);
            menuRoot.setChildMenus(menuTreeWithoutRoot);
            return ApiResult.success("根据登录账号查询菜单列表成功！", menuRoot);
        } catch (Exception e) {
            return ApiResult.fail("根据登录账号查询菜单列表失败！");
        }
    }

    public static List<AuthMenuInfoEntity> getMenuTreeByList(List<AuthMenuInfoEntity> menuList,
                                                             AuthMenuInfoEntity superDpt) {
        List<AuthMenuInfoEntity> tree = new ArrayList<AuthMenuInfoEntity>();
        String rootCode = superDpt.getMenuCode();
        if (1 == menuList.size()) {
            return menuList;
        }
        for (AuthMenuInfoEntity menu : menuList) {
            String superCode = menu.getParentMenuCode();
            if (rootCode.equals(superCode)) {
                String code = menu.getMenuCode();
                if (null != code) {
                    List<AuthMenuInfoEntity> childList = getMenuTreeByList(menuList, menu);
                    if (childList != null && !childList.isEmpty()) {
                        menu.setChildMenus(childList);
                    }
                }
                tree.add(menu);
            }
        }
        return tree;
    }

    @Override
    public ApiResult<?> queryPaasUrlByUserId(String userId) throws Exception {
        ApiResult apiResult = authPasswordService.queryPlainPasswordByUserId(userId);
        String password = (String) apiResult.getData();
//        String casServerUrl = "http://100.5.14.80:30081/sso/tickets";//cas Server的地址
//        String serviceUrl ="http://100.5.14.80:30085/core";//服务的地址
        String casServerUrl = PropertyUtil.getValue("application.properties", "paas.cas.server.url");//cas Server的地址
        String serviceUrl = PropertyUtil.getValue("application.properties", "paas.service.url");//服务的地址
        String ST = CasRestClient.validateFromCAS(userId, password, casServerUrl, serviceUrl);
        if (StringUtil.isNull(ST)) {
            return ApiResult.fail("暂时无法访问，请联系管理员！");
        }
        String url = serviceUrl + "?ticket=" + ST;
        return ApiResult.success("", url);
    }

    @Override
    public ApiResult<?> queryMenuUrlByUserId(String userId) {
        //按照账号查询菜单列表
        List<AuthMenuInfoEntity> menuList = authMenuInfoEntityMapper.queryUrlByUserId(userId);
        return ApiResult.success("根据登录账号查询菜单列表成功！", menuList);
    }


    //菜单新增及修改时，关联添加按钮及对应URL
    public void insertPageMenu(List<AuthMenuInfoEntity> pageList, String menuCode, String userId) {

        int i = 1;
        for (AuthMenuInfoEntity entity : pageList) {
            entity.setId(UUIDUtil.absNumUUID());
            entity.setParentMenuCode(menuCode);
            entity.setStatus("0");
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setCreateUser(userId);
            //根据父级编码生成新权限编码
            String newMenuCode = "";
            Map<String, Object> codeQueryMap = new HashMap<>();
            codeQueryMap.put("parentMenuCode", menuCode);
            List<AuthMenuInfoEntity> menuInfoEntityList = authMenuInfoEntityMapper.queryMaxMenuCodeByParentCode(codeQueryMap);
            newMenuCode = getMenuCode(menuCode, menuInfoEntityList);
            entity.setMenuCode(newMenuCode);
            i++;
            authMenuInfoEntityMapper.insert(entity);
            //新增入超管和menu_info的关联记录到auth_role_menu表
            AddMenuRole(entity);
        }
    }

    /**
     * 获取菜单code的方法
     *
     * @param
     * @return
     */
    private String getMenuCode(String menuCode, List<AuthMenuInfoEntity> menuInfoEntityList) {
        String newMenuCode;
        if (menuInfoEntityList.isEmpty()) {
            newMenuCode = menuCode + "001";
        } else {
            //父节点下有子节点
            String maxCode = menuInfoEntityList.get(0).getMenuCode();
            BigInteger nextMenuCode = new BigInteger(maxCode);
            nextMenuCode = nextMenuCode.add(new BigInteger("1"));
            AuthMenuInfoEntity menuEntity = new AuthMenuInfoEntity();
            menuEntity.setMenuCode(menuCode);
            //菜单code已经存在，则递归生成新的菜单code
            int repeatCount = authMenuInfoEntityMapper.selectCount(menuEntity);
            while (repeatCount > 0) {
                nextMenuCode = nextMenuCode.add(new BigInteger("1"));
                String nextCode = String.valueOf(nextMenuCode);
                menuEntity.setMenuCode(nextCode);
                repeatCount = authMenuInfoEntityMapper.selectCount(menuEntity);
            }
            newMenuCode = String.valueOf(nextMenuCode);
        }
        return newMenuCode;
    }

    /**
     * 修改菜单页面
     *
     * @param
     * @return
     */
    public void updatePageMenu(List<AuthMenuInfoEntity> pageList, String menuCode, String userId) {

        for (AuthMenuInfoEntity entity : pageList) {
            if (Const.UPDATE.equals(entity.getFunctionType())) {
                entity.setParentMenuCode(menuCode);
                entity.setUpdateUser(userId);
                authMenuInfoEntityMapper.updateByPrimaryKey(entity);
            } else if (Const.ADD.equals(entity.getFunctionType())) {
                entity.setId(UUIDUtil.absNumUUID());
                entity.setParentMenuCode(menuCode);
                entity.setStatus("0");
                entity.setCreateTime(new Date());
                entity.setUpdateTime(new Date());
                entity.setCreateUser(userId);
                //根据父级编码生成新权限编码
                String newMenuCode = "";
                Map<String, Object> codeQueryMap = new HashMap<>();
                codeQueryMap.put("parentMenuCode", menuCode);
                List<AuthMenuInfoEntity> menuInfoEntityList = authMenuInfoEntityMapper.queryMaxMenuCodeByParentCode(codeQueryMap);
                newMenuCode = getMenuCode(menuCode, menuInfoEntityList);
                entity.setMenuCode(newMenuCode);
            } else {
                authMenuInfoEntityMapper.deleteByPrimaryKey(entity);
            }
        }
    }

    /**
     * 根据菜单码获取菜单详情（菜单对应的按钮及URL信息）
     *
     * @param id 菜单主键.
     * @return result.
     */
    @Override
    public AuthMenuBean queryMenuButtonByMenuCode(String id) {
        AuthMenuBean authMenuBean = new AuthMenuBean();
        AuthMenuInfoEntity authMenuInfoEntity = new AuthMenuInfoEntity();
        authMenuInfoEntity.setId(id);
        authMenuInfoEntity = authMenuInfoEntityMapper.selectByPrimaryKey(authMenuInfoEntity);
        //功能菜单须获取对应的子页面菜单列表
        if (null != authMenuInfoEntity && Const.FUNCTIONMENUNUM.equals(authMenuInfoEntity.getMenuType())) {
            List<AuthMenuInfoEntity> pageList = authMenuInfoEntityMapper.
                    queryMenuButtonByMenuCode(authMenuInfoEntity.getMenuCode());
            authMenuBean.setPageList(pageList);
        }
        authMenuBean.setAuthMenuInfoEntity(authMenuInfoEntity);
        return authMenuBean;
    }

    @Override
    public List<LoginMenuBean> queryLoginMenuByUserId(String userId) {
        return authMenuInfoEntityMapper.queryLoginMenuByUserId(userId);
    }

    @Override
    public List<LoginMenuBean> queryLoginMenuByAdmin() {
        return authMenuInfoEntityMapper.queryLoginMenuByAdmin();
    }

    /**
     * 新增角色时获取菜单列表
     *
     * @param userId
     * @return
     */
    @Override
    public ApiResult<?> getMenuList(String userId) {

        List<AuthMenuInfoEntity> menuList = new ArrayList<>();
        AuthRoleInfoEntity entity = authRoleMenuServiceImpl.getMinRoleKey(userId);
        try {
            menuList = authMenuInfoEntityMapper.queryMenuListByRoleId(entity.getId());
            //将查到的菜单列表拼装成树
            AuthMenuInfoEntity menuRoot = authMenuInfoEntityMapper.queryRootMenu();
            List<AuthMenuInfoEntity> menuTreeWithoutRoot = getMenuTreeByList(menuList, menuRoot);
            menuRoot.setChildMenus(menuTreeWithoutRoot);
            return ApiResult.success("新增角色时获取菜单列表成功！", menuRoot.getChildMenus());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_FAILD, "新增角色时获取菜单列表失败!");
        }
    }
}
