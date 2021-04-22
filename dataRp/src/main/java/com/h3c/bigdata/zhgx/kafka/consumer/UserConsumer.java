package com.h3c.bigdata.zhgx.kafka.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.dao.AuthRoleInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.dao.AuthUserInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserRoleEntity;
import com.h3c.bigdata.zhgx.function.system.model.UserWithUserRoleListModel;
import com.h3c.bigdata.zhgx.function.system.model.UsersStatusBean;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserRoleService;
import com.h3c.bigdata.zhgx.function.system.service.AuthUserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 消费消息，并将解析出的用户信息插入到用户信息表及角色表中
 * @date 2019-6-4 18:17:56
 * @author y19714
 */
@Component
public class UserConsumer {

    private Logger log = LoggerFactory.getLogger(UserConsumer.class);

    @Autowired
    private AuthUserRoleService authUserRoleService;
    @Autowired
    private AuthUserService sysUserService;
    @Autowired
    private AuthUserInfoEntityMapper authUserInfoEntityMapper;
    @Autowired
    private AuthRoleInfoEntityMapper authRoleInfoEntityMapper;

    @KafkaListener(topics = {"TOPIC_SYNC_RESOURCECATALS"})
    public void consumer(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        log.info("开始消费消息FROM TOPIC----"+record.topic());
        if(kafkaMessage.isPresent()){

            //获取消息体并解析
            Object message = kafkaMessage.get();
            String str = message.toString();
            JSONObject json = JSONObject.parseObject(str);
            //获取操作类型
            String operType = json.getString("syncType");
            JSONArray jsonArray = json.getJSONArray("data");
            Map<String, Object> map = (Map<String, Object>)jsonArray.get(0);
            String userName = (String) map.get("userName");
            String phoneNumber = (String) map.get("phoneNumber");
            String loginName = (String) map.get("loginName");
            String sex = (String) map.get("sex");
            String email = (String) map.get("email");
            String departmentId = (String) map.get("deptId");
            List<String> roleTypeList = (List<String>) map.get("roleTypes");
            AuthUserInfoEntity rcSysUserEntity = new AuthUserInfoEntity();
            UserWithUserRoleListModel userWithUserRoleListModel = new UserWithUserRoleListModel();
            UsersStatusBean usersStatusBean = new UsersStatusBean();
            List<AuthUserInfoEntity> rcSysUserEntities = new ArrayList<>();
            //从消息中获取信息，并放入用户信息实体类中
            rcSysUserEntity.setUserName(userName);
            rcSysUserEntity.setPhoneNumber(phoneNumber);
            rcSysUserEntity.setUserId(loginName);
            rcSysUserEntity.setEmailAddress(email);
            rcSysUserEntity.setSex(sex);
            rcSysUserEntity.setDepartmentId(departmentId);

            //根据登录名查询出当前用户ID，并set进用户实体类进行数据更新及数据的删除
            AuthUserInfoEntity sysUserEntity= authUserInfoEntityMapper.selectUserByUserId(loginName);

            try {
                //获取操作类型，根据操作类型调用相应的方法
                if(operType.equals("create")){
                    log.info("开始创建用户信息");
                    //如果用户已经存在，但是为禁用状态，则将其状态置为启用
                    if(sysUserEntity!=null){
                        if(sysUserEntity.getStatus().equals("1")) {
                            log.info("用户已存在，当前状态为禁用状态，开始启用");
                            usersStatusBean.setStatus("0");
                            rcSysUserEntity.setId(sysUserEntity.getId());
                            rcSysUserEntities.add(rcSysUserEntity);
                            usersStatusBean.setUsers(rcSysUserEntities);
                            ApiResult apiResult = sysUserService.updateUsersStatus(usersStatusBean);
                            log.info(apiResult.getMessage());
                        }else{
                           log.info(loginName+"用户已存在");
                        }
                    }else {
                        //增加用户角色信息，将其设置为管理员权限

                        rcSysUserEntity.setStatus("0");
                        List<AuthUserRoleEntity> authUserRoleEntityList =getRoleIdList(roleTypeList);
                        userWithUserRoleListModel.setAuthUserInfoEntity(rcSysUserEntity);
                        userWithUserRoleListModel.setAuthUserRoleEntityList(authUserRoleEntityList);
                        ApiResult apiResult = authUserRoleService.addUseWithRoleList(userWithUserRoleListModel,"");
                        log.info(userName + apiResult.getMessage());
                    }

                }else if(operType.equals("update")){
                    log.info("开始更新用户信息");
                    if(sysUserEntity!=null){
                        rcSysUserEntity.setId(sysUserEntity.getId());
                      //  ApiResult apiResult =authUserService.updateUserInfoForMiddlePlatform(rcSysUserEntity);
                      //  log.info(apiResult.getMessage());
                    }else {
                        log.info(loginName+"该用户不存在");
                    }

                }if (operType.equals("delete")){
                    log.info("开始删除用户信息");
//                    if(sysUserEntity!=null) {
//                        //将用户状态设置为禁用
//                        usersStatusBean.setStatus("1");
//                        rcSysUserEntity.setId(sysUserEntity.getId());
//                        rcSysUserEntities.add(rcSysUserEntity);
//                        usersStatusBean.setUsers(rcSysUserEntities);
//                        ApiResult apiResult = sysUserService.updateUsersStatus(usersStatusBean);
//                        log.info(apiResult.getMessage());
//                    }else {
//                        log.info(loginName+"该用户不存在");
//                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage()+"消费消息失败");
            }

        }

    }

    /**
     * @Description: 将统一平台的角色类型列表转换为填报系统角色表的角色列表
     * @Author: wuYaBing
     * @param
     * @return: null
     * @Date: 2020/6/23 16:49
     **/
    private List<AuthUserRoleEntity> getRoleIdList( List<String> roleTypeList){
        List<AuthUserRoleEntity> roleList = new ArrayList<>();
        for (String roleType : roleTypeList){
            AuthUserRoleEntity entity = new AuthUserRoleEntity();
            String roleId="";
            switch (roleType){
                case "0":
                case "1":
                    roleId = authRoleInfoEntityMapper.queryRoleByRoleKey(roleType);
                    break;
                case "50":
                    roleId = authRoleInfoEntityMapper.queryRoleByRoleKey("2");
                    break;
                default:
                    roleId = authRoleInfoEntityMapper.queryRoleByRoleKey("3");
            }
            entity.setRoleId(roleId);
            roleList.add(entity);
        }
        return roleList;
    }
}
