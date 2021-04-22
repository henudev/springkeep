package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.DictData;

import java.io.Serializable;

/**
 * 字典数据查询结果封装Bean.
 *
 * @Author J16898
 * @Date 2018/8/8
 * @Version 1.0
 */
public class DictDataResultBean extends DictData implements Serializable {

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    /**
     * 字典类型名称.
     */
    private String dictTypeName;
}
