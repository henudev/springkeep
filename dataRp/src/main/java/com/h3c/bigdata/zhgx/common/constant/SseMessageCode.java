package com.h3c.bigdata.zhgx.common.constant;

import java.io.Serializable;

/**
 * @author lvyacong
 * @date 2019/12/17 8:55
 */
public class SseMessageCode implements Serializable {

    public SseMessageCode() {
    }

    /**
     * serialVersionUID.
     */
    public static final Long serialVersionUID = 1L;
    /**
     * 数据申请
     */
    public static final String DATA_APPLY = "16001";
    /**
     * 数据申请批准
     */
    public static final String DATA_APPLY_APPROVED = "16002";
    /**
     * 资源订阅申请
     */
    public static final String RESOURCE_SUBSCRIBE_APPLY = "16003";

    /**
     * 资源订阅审批通过
     */
    public static final String RESOURCE_SUBSCRIBE_APPROVED = "16004";

    /**
     * 资源订阅审批催办
     */
    public static final String RESOURCE_SUBSCRIBE_WARN = "16005";

    /**
     * 资源订阅驳回
     */
    public static final String RESOURCE_SUBSCRIBE_REJECT = "16006";


}
