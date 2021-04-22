package com.h3c.bigdata.zhgx.function.report.model;

import lombok.Data;

/**
 * sse消息推送返回实体
 *
 * @author lvyacong
 * @date 2019/12/17 8:51
 */
@Data
public class SseResultModel {

    /**
     * 消息类型：message,approve,warn
     */
    private String type;
    /**
     * 消息代码
     */
    private String messageCode;
    /**
     * 时间戳
     */
    private String timestamp;

    @Override
    public String toString() {
        return "{" +
                "type:" + type + ", messageCode:" + messageCode + ", timestamp:" + timestamp + "}";
    }
}
