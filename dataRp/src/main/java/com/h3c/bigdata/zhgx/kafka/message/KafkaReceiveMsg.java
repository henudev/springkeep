package com.h3c.bigdata.zhgx.kafka.message;


/**
 * @Title: KafkaReceiveMsg
 * @ProjectName platform
 * @Description: kafka接收第三方用户的消息体
 * @date 2019/4/2 15:25
 */
public class KafkaReceiveMsg extends SyncDataBody {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
