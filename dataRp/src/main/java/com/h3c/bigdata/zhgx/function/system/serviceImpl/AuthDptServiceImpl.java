package com.h3c.bigdata.zhgx.function.system.serviceImpl;

import com.h3c.bigdata.zhgx.common.constant.ErrorCode;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.report.dao.TemplateEntityMapper;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import com.h3c.bigdata.zhgx.function.system.dao.AuthDepartmentInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartLocInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthRoleInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.model.DepartmentInfoResultBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @Author: h17338
 * @Description: 部门维护类实现
 * @Date: 2018/7/30
 */
@Service("authDptService")
public class AuthDptServiceImpl extends BaseService implements AuthDptService {

    /**
     * 日志记录.
     */
    private Logger log = LoggerFactory.getLogger(AuthDptServiceImpl.class);

    @Autowired
    private AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper;

    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;



    @Autowired
    private AuthRoleMenuServiceImpl authRoleMenuService;



    @Resource
    private TemplateEntityMapper templateEntityMapper;


    @Override
    public ApiResult<?> addDptInfo(AuthDepartmentInfoEntity departmentEntity) {
        try{
            //校验必传参数
            String parentDptCode = departmentEntity.getParentDepartmentCode();
            if (StringUtil.isNull(parentDptCode) ||  parentDptCode.equals("")) {
                return ApiResult.fail("父级部门编码不能为空！");
            }
            String dptName = departmentEntity.getDepartmentName();
            if (StringUtil.isNull(dptName) ||  dptName.equals("")) {
                return ApiResult.fail("部门名称不能为空！");
            }
            //校验父节点是否存在
            AuthDepartmentInfoEntity parentDptEntity = new AuthDepartmentInfoEntity();
            parentDptEntity.setStatus("0");
            parentDptEntity.setDepartmentCode(parentDptCode);
            int parentDptCount = authDepartmentInfoEntityMapper.selectCount(parentDptEntity);
            if(parentDptCount == 0) {
                return ApiResult.fail("父级部门不存在！");
            }
            if(parentDptCount > 1) {
                return ApiResult.fail("存在多条相同的父级部门编码！");
            }
            //同级节点不能同名校验
            AuthDepartmentInfoEntity dptRepeatNameEntity = new AuthDepartmentInfoEntity();
            dptRepeatNameEntity.setStatus("0");
            dptRepeatNameEntity.setParentDepartmentCode(parentDptCode);
            dptRepeatNameEntity.setDepartmentName(dptName);
            int repeatNameCount = authDepartmentInfoEntityMapper.selectCount(dptRepeatNameEntity);
            if(repeatNameCount > 0) {
                return ApiResult.fail("该节点下部门已存在！");
            }
            //根据父级编码，查询该父节点下子节点code最大的节点值，生成新code
            String dptCode="";
            Map<String, Object>  dptCodeQueryMap =new HashMap<>();
            dptCodeQueryMap.put("parentDepartmentCode",parentDptCode);
            List<AuthDepartmentInfoEntity>  dptInfoEntityList = authDepartmentInfoEntityMapper.queryMaxDptCodeByParentCode(dptCodeQueryMap);
            if(dptInfoEntityList.isEmpty()){
                //父节点下无子节点，四位数字一层，生成新节点编码
                dptCode = parentDptCode+"0001";
            } else {
                //父节点下有子节点
                String maxCode = dptInfoEntityList.get(0).getDepartmentCode();
                BigInteger nextOrgaCode = new BigInteger(maxCode);
                nextOrgaCode = nextOrgaCode.add(new BigInteger("1"));
                dptCode = String.valueOf(nextOrgaCode);
            }
            departmentEntity.setDepartmentCode(dptCode);
            //插入表中
            departmentEntity.setCreateTime(new Date());
            departmentEntity.setStatus("0");
            if (StringUtils.isBlank(departmentEntity.getId())){
                departmentEntity.setId(UUIDUtil.createUUID());
            }
            authDepartmentInfoEntityMapper.insert(departmentEntity);
            return ApiResult.success("新增部门信息成功",departmentEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.SAVE_DATA_FAILED, "新增部门信息失败!");
        }
    }

    @Override
    public ApiResult<?> updateDptInfo(AuthDepartmentInfoEntity departmentEntity) {
        try{
            //校验必传参数
            String parentDptCode = departmentEntity.getParentDepartmentCode();
            if (StringUtil.isNull(parentDptCode) ||  parentDptCode.equals("")) {
                return ApiResult.fail("父级部门编码不能为空！");
            }
            String dptName = departmentEntity.getDepartmentName();
            if (StringUtil.isNull(dptName) ||  dptName.equals("")) {
                return ApiResult.fail("部门名称不能为空！");
            }
            String id= departmentEntity.getId();
            if(StringUtil.isNull(id) ||  id.equals("")) {
                return ApiResult.fail("部门id不能为空！");
            }
            String departmentCode= departmentEntity.getDepartmentCode();
            if(StringUtil.isNull(departmentCode) ||  departmentCode.equals("")) {
                return ApiResult.fail("部门code不能为空！");
            }
            //若逻辑删除部门，则需校验该节点下是否存在启用状态的子节点部门、用户账号
            String status= departmentEntity.getStatus();
            if(StringUtil.isNotNull(status) && "1" == status) {
                //校验该节点下是否存在启用状态的子节点
                AuthDepartmentInfoEntity dptEntity = new AuthDepartmentInfoEntity();
                dptEntity.setParentDepartmentCode(departmentCode);
                dptEntity.setStatus("0");
                int childrenDptCount = authDepartmentInfoEntityMapper.selectCount(departmentEntity);
                if(childrenDptCount > 0) {
                    return ApiResult.fail("该部门节点下存在启用状态的子节点部门！");
                }
                //校验该部门是否有启用状态的用户账号
                AuthUserInfoEntity userEntity = new AuthUserInfoEntity();
                userEntity.setDepartmentId(id);
                userEntity.setStatus("0");
                int userCount = authUserInfoEntityMapper.selectCount(userEntity);
                if(userCount > 0) {
                    return ApiResult.fail("该部门节点下存在启用状态的用户账号！");
                }
            }
            departmentEntity.setUpdateTime(new Date());
            authDepartmentInfoEntityMapper.updateByPrimaryKey(departmentEntity);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "部门基本信息更新失败!");
        }
        return ApiResult.success("部门基本信息更新成功！");
    }

    @Override
    public ApiResult<?> queryDptInfo(int page, int pageSize, String field, String dir, AuthDepartmentInfoEntity departmentEntity) {
        List<DepartmentInfoResultBean> authDepartmentInfoEntityList;
        PageResult result;
        try {
            startPage(page,pageSize,field,dir);
            authDepartmentInfoEntityList = authDepartmentInfoEntityMapper.queryDptInfoEntityList(departmentEntity);
            result = getDataList(authDepartmentInfoEntityList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.QUERY_DATA_FAILED, "部门基本信息查询失败!");
        }
        return ApiResult.success("部门基本信息查询成功！",result);
    }

    @Override
    public ApiResult<?> queryDptInfoPinpoint(AuthDepartmentInfoEntity departmentInfoEntity,int page, int pageSize, String field, String dir) {
        List<AuthDepartmentInfoEntity> depts;
        PageResult result;
        try {
            startPage(page,pageSize,field,dir);
            depts = authDepartmentInfoEntityMapper.select(departmentInfoEntity);
            result = getDataList(depts);
            return ApiResult.success("部门信息查询成功！", result);
        }catch (Exception e) {
            return ApiResult.fail("部门信息查询失败！");
        }
    }

    /**
     * 删除、批量删除组织部门
     *
     * @param
     * @return
    */
    @Override
    public ApiResult<?> deleteDptsStatus(List<AuthDepartmentInfoEntity> dpts) {
        try{
            //校验部门下是否有在用部门
            List<AuthDepartmentInfoEntity> dptCheck = authDepartmentInfoEntityMapper.queryChildrenDptsByParentId(dpts);
            if(!dptCheck.isEmpty()) {
                return ApiResult.fail("请先删除该部门下的子部门！");
            }
            //校验部门下是否有用户账号关联
            List<AuthUserInfoEntity> userCheck = authUserInfoEntityMapper.selectUserByDptId(dpts);
            if(!userCheck.isEmpty()) {
                return ApiResult.fail("请先删除该部门下的用户！");
            }
            //批量更新部门状态为停用
            authDepartmentInfoEntityMapper.deleteDptsStatus(dpts);

            return ApiResult.success("批量禁用部门成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "批量禁用部门失败!");
        }
    }


    /**
     * 数据申请页面的信息 包括用户信息和叶子部门信息
     *
     * @return
     */
    @Override
    public ApiResult getDataApplyInfo(String userId) {

        AuthUserInfoEntity authUserInfoEntity = authUserInfoEntityMapper.selectUserByUserId(userId);
        if(authUserInfoEntity == null){
            return ApiResult.fail(ErrorCode.PARAM_ERROR,"参数传递错误");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("applyName",authUserInfoEntity.getUserName());
        map.put("applyEmail",authUserInfoEntity.getEmailAddress());
        map.put("applyPhone",authUserInfoEntity.getPhoneNumber());

        return ApiResult.success(map);
    }


    /**
     * 变更部门地理信息
     * */
    @Override
    public ApiResult<?> updateLocDptInfo(AuthDepartmentInfoEntity departmentEntity) {

        AuthDepartLocInfoEntity authDepartLocInfoEntity=new AuthDepartLocInfoEntity();


        String id= departmentEntity.getId();
        if (StringUtil.isNull(id) ||  id.equals("")) {
            return ApiResult.fail("部门id不能为空！");
        } else {
            authDepartLocInfoEntity.setId(id);
        }



        String lng= departmentEntity.getLng();
        if (StringUtil.isNull(lng) ||  lng.equals("")) {
            return ApiResult.fail("部门Lng不能为空！");
        } else {
            authDepartLocInfoEntity.setLng(lng);
        }

        String lat= departmentEntity.getLat();
        if (StringUtil.isNull(lat) ||  lng.equals("")) {
            return ApiResult.fail("部门Lat不能为空！");
        } else {
            authDepartLocInfoEntity.setLat(lat);
        }

        try{

        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ErrorCode.UPDATE_DATA_FAILED, "部门基本信息更新失败!");
        }
        return ApiResult.success("部门地理信息更新成功！");
    }


    public AuthDepartmentInfoEntity selectDeptByUserId(String userId){
        return  authDepartmentInfoEntityMapper.selectDeptByUserId(userId);
    }
    /**
     * @param
     * @return
     * @Description: 根据部门id获取本部门及子部门列表
     * @Author: w15112
     * @Date: 2019/8/22 15:17
     */
    public List<String> getDepListById(String departmentId) {
        List<String> deptList = new ArrayList<>();
        AuthDepartmentInfoEntity department = authDepartmentInfoEntityMapper.selectDepartmentById(departmentId);
        String departmentCode = department.getDepartmentCode();
        deptList = authDepartmentInfoEntityMapper.selectDepartmentIdsByCode(departmentCode);
        deptList.add(departmentId);
        return deptList;
    }

    @Override
    public List<String> getDeprCodeListByDeptId(String departmentId) {
        List<String> departmentCodeList = new ArrayList<>();
        AuthDepartmentInfoEntity department = authDepartmentInfoEntityMapper.selectDepartmentById(departmentId);
        String departmentCode = department.getDepartmentCode();
        departmentCodeList = authDepartmentInfoEntityMapper.selectDepartmentCodesByCode(departmentCode);
        departmentCodeList.add(departmentCode);
        return departmentCodeList;
    }

    /**
     * @Description: 普通用户/部门管理员根据用户最小roleKey获取对应的部门及子部门列表,系统管理员/平台开发者为null
     * @param
     * @return
     * @Author: w15112
     * @Date: 2019/8/22 15:58
     */
    public List<String> getDepListByRoleKey(String departmentId, String userId) {
        List<String> deptList = new ArrayList<>();
        AuthRoleInfoEntity entity = authRoleMenuService.getMinRoleKey(userId);
        if (null != entity && Integer.valueOf(entity.getRoleKey()) < 2) {
            deptList = null;
        }else {
            deptList = getDepListById(departmentId);
        }
        return deptList;
    }
    /**
     * @Description: 校验部门是否存在表格或者类目
     * @param
     * @return
     */
    @Override
    public ApiResult<?> checkDepExistTableOrCategory(String depId) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("table",false);
        map.put("category",false);

        //校验部门下是否存在表格
        List<String> dptIdList = new ArrayList<>();
        dptIdList.add(depId);
        List<TemplateModel> templateList = templateEntityMapper.selectAllByKeyword(null, dptIdList);
        if (templateList.size() > 0) {
            map.put("table",true);
        }
        //校验该部门是否存在关联类目组织
        AuthDepartmentInfoEntity dptInfoEntity = new AuthDepartmentInfoEntity();
        dptInfoEntity.setStatus("0");
        dptInfoEntity.setId(depId);
        List<DepartmentInfoResultBean> list = authDepartmentInfoEntityMapper.queryDptInfoEntityList(dptInfoEntity);

        return ApiResult.success("部门校验完成！", map);
    }
}
