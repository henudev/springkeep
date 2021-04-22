package com.h3c.bigdata.zhgx.common.exception;

/**
 * @Description 记录不存在异常，用以查数据库之后结果为null或者这size()==0
 * @Author l17561
 * @Date 2019/4/11 15:47
 * @Version V1.0
 */
public class RecordNotExistException extends GlobalException {

    public RecordNotExistException() {
        this("未查到对应的数据记录！");
    }

    public RecordNotExistException(String message) {
        super(message);
    }
}
