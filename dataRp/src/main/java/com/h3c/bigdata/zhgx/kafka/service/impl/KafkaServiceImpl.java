package com.h3c.bigdata.zhgx.kafka.service.impl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.h3c.bigdata.zhgx.function.system.entity.AuthPasswordEntity;
import com.h3c.bigdata.zhgx.function.system.entity.AuthUserInfoEntity;
import com.h3c.bigdata.zhgx.kafka.constant.KafkaConstant;
import com.h3c.bigdata.zhgx.kafka.message.KafkaReceiveMsg;
import com.h3c.bigdata.zhgx.kafka.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: KafkaServiceImpl
 * @ProjectName platform
 * @Description: 用户信息同步中台
 * @date 2019/2/27 15:09
 */
@Service
public class KafkaServiceImpl implements KafkaService {
    private static final Logger log = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${kafka.app.topic.foo}")
    String appTopic;

    private static Gson gson = new GsonBuilder().create();

    @Override
    public String syncUserInfo(String type, AuthUserInfoEntity sysUser, AuthPasswordEntity authPasswordEntity) {

        KafkaReceiveMsg kafkaReceiveMsg = new KafkaReceiveMsg();
        kafkaReceiveMsg.setSyncType(type);
        //用户消息体
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        //重置密码时，sysUser为空，从authPasswordEntity去用户id
        if (null == sysUser){
            map.put("loginName", authPasswordEntity.getUserId());
        }else{
            map.put("userNum", sysUser.getUserId());
            map.put("loginName", sysUser.getUserId());
            map.put("userName", sysUser.getUserName());
            map.put("email", sysUser.getEmailAddress());
            map.put("phoneNumber", sysUser.getPhoneNumber());
            map.put("sex", sysUser.getSex());
            map.put("status", sysUser.getStatus());
        }

        if (null != authPasswordEntity){
            map.put("salt", authPasswordEntity.getSalt());
            map.put("passwd", authPasswordEntity.getPassowrd());
        }
        list.add(map);
        kafkaReceiveMsg.setData(list);
        try {
            kafkaTemplate.send(appTopic, gson.toJson(kafkaReceiveMsg));
            log.info("==topic:" + appTopic + "==kafka发送的消息体:" + gson.toJson(kafkaReceiveMsg));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info(KafkaConstant.SEND_ERROE);
        }

        return KafkaConstant.SEND_SUCCESS;

    }

}
