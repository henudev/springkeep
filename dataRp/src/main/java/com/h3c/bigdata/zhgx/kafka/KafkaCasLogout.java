package com.h3c.bigdata.zhgx.kafka;

import com.alibaba.fastjson.JSONObject;
import com.h3c.bigdata.zhgx.common.utils.LoginUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaCasLogout {

    private Logger log = LoggerFactory.getLogger(KafkaCasLogout.class);

    @KafkaListener(topics = {"TOPIC_CAS_LOGOUT_USER"})
    public void casLogout(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        log.info("开始消费消息FROM TOPIC----" + record.topic());
        if (kafkaMessage.isPresent()) {

            //获取消息体并解析
            Object message = kafkaMessage.get();
            String str = message.toString();
            JSONObject json = JSONObject.parseObject(str);
            String loginName = json.getString("loginName");
            log.info("开始退出"+loginName);
            LoginUtil.removeCache(loginName);
            log.info("用户"+loginName+"已退出");
        }

    }
}
