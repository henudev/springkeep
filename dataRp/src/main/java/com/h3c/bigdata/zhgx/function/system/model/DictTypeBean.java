package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.DictType;

public class DictTypeBean extends DictType {
    /**字典下关联的条数*/

    private Integer dictCount;

    /**
     * 获取 字典总数
     */
    public Integer getDictCount() {
        return dictCount;
    }

    /**
     * 设置 字典总数
     * @param dictCount
     */
    public void setDictCount(Integer dictCount) {
        this.dictCount = dictCount;
    }
}
