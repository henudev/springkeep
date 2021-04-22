package com.h3c.bigdata.zhgx.function.system.model;


/**
 * @Author:f13979
 * @Description: 收藏接口的bean类
 * @Date:Created in 15:14 18/8/7
 * @Modified by:
 */
public class CollectBean extends ReqBaseIdBean{

    //状态，取消收藏还是收藏  0表示要取消收藏，1表示要收藏
    private String status;
    //收藏类型，收藏的是数据目录还是接口  0数据目录  1数据接口
    private String collectType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }
}
