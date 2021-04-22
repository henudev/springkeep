package com.h3c.bigdata.zhgx.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.system.dao.AuthDepartmentInfoEntityMapper;
import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import com.h3c.bigdata.zhgx.function.system.service.AuthDptService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 消费消息，使本地组织机构与消息同步
 * TOPIC_SYNC_DEPT_ALL
 * TOPIC_SYNC_DEPT_ADD
 * TOPIC_SYNC_DEPT_UPDATE
 * TOPIC_SYNC_DEPT_DELETE
 */
@Component
public class DepartmentConsumer {

    private Logger log = LoggerFactory.getLogger(DepartmentConsumer.class);

    private AuthDptService authDptService;

    private AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper;


    @KafkaListener(topics = {"TOPIC_SYNC_DEPT_ADD"})
    private void add(ConsumerRecord<?, ?> record){
        log.info("==> 开始消费消息 topic: "+record.topic());

        // 1 解析消息
        String creditCode;
        String departmentName = null;
        String id = null;
        String parentDeptId = null;
        String status;
        String updateTime;
        String updateUser;
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){

            String message = kafkaMessage.get().toString();
            JSONObject jsonObject = JSONObject.parseObject(message);
            creditCode = jsonObject.getString("creditCode");
            departmentName = jsonObject.getString("departmentName");
            id = jsonObject.getString("id");
            parentDeptId = jsonObject.getString("parentDeptId");
            status = jsonObject.getString("status");
            updateTime = jsonObject.getString("updateTime");
            updateUser = jsonObject.getString("updateUser");
        }

        // 2 在本系统新增组织
        // 2.1 获取父级组织
        AuthDepartmentInfoEntity parentDepartment = authDepartmentInfoEntityMapper.selectByPrimaryKey(parentDeptId);
        if(parentDepartment == null){
            log.error("==> 自kafka消息topic:"+record.topic()+" 新增组织："+departmentName+"失败。原因：父级组织不存在。");
            return;
        }

        // 2.2 保存
        AuthDepartmentInfoEntity department = new AuthDepartmentInfoEntity();
        department.setParentDepartmentCode(parentDepartment.getDepartmentCode());
        department.setDepartmentName(departmentName);
        department.setId(id);
        ApiResult<?> apiResult = authDptService.addDptInfo(department);
        if(apiResult.getCode().equals(Const.kCode_Success)){
            log.info("==> 自kafka消息topic:"+record.topic()+" 新增组织："+departmentName+"成功。");
        }else {
            log.error("==> 自kafka消息topic:"+record.topic()+" 新增组织："+departmentName+"失败。");
        }

    }

    @KafkaListener(topics = "TOPIC_SYNC_RESOURCECATALS")
    public void update(ConsumerRecord<?, ?> record){
        log.info("==> 开始消费消息 topic: "+record.topic());
        Optional<?> optional = Optional.ofNullable(record.value());
        if(optional.isPresent()){
            String message = optional.get().toString();
            JSONObject jsonObject = JSONObject.parseObject(message);

        }
    }

    @KafkaListener(topics = "TOPIC_SYNC_DEPT_DELETE")
    public void delete(ConsumerRecord<?, ?> record){
        log.info("==> 开始消费消息 topic: "+record.topic());
        Optional<?> optional = Optional.ofNullable(record.value());
        if(optional.isPresent()){
            String message = optional.get().toString();
            JSONObject jsonObject = JSONObject.parseObject(message);
            List<AuthDepartmentInfoEntity> dpts= new ArrayList<>();
            String depId =  jsonObject.getString("deptId");
            AuthDepartmentInfoEntity entity = new AuthDepartmentInfoEntity();
            entity.setId(depId);
            dpts.add(entity);
            ApiResult<?> apiResult =  authDptService.deleteDptsStatus(dpts);
            if(apiResult.getCode().equals(Const.kCode_Success)){
                log.info("==> 自kafka消息topic:"+record.topic()+" 新增组织："+depId+"成功。");
            }else {
                log.error("==> 自kafka消息topic:"+record.topic()+" 新增组织："+depId+"失败。");
            }
        }
    }


    @Autowired
    public void SetAuthDptService(AuthDptService authDptService){
        this.authDptService = authDptService;
    }

    @Autowired
    public void setAuthDepartmentInfoEntityMapper(AuthDepartmentInfoEntityMapper authDepartmentInfoEntityMapper){
        this.authDepartmentInfoEntityMapper = authDepartmentInfoEntityMapper;
    }

}
