package com.h3c.bigdata.zhgx.kafka.constant;

/**
 * @Title: KafkaConstant
 * @ProjectName platform
 * @Description: TODO
 * @date 2019/4/2 10:32
 */
public class KafkaConstant {

    /**
     * 同步用户所用
     */
    public static final String TOPIC_SYNC_CAS_USER_CHANGE_SYS = "TOPIC_CAS_USER_CHANGE_SYS";

    /**
     * 同步消息所用
     */
    public static final String TOPIC_SYNC_MSG_CHANGE_SYS = "TOPIC_SYNC_MSG_CHANGE_SYS";

    public static final String  ADD = "add";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    /**
     * 操作类型
     */
    public static final String NOT_EXIST_OPERATE_TYPE = "无此操作类型.";

    /**
     * kafka发送消息异常提醒
     */
    public static final String NOT_EXIST_USERID = "userId 为空！";
    public static final String NOT_EXIST_USER_APPID = "该用户没有对应的应用！";

    public static final String SEND_SUCCESS = "kafka消息体 发送成功！";

    public static final String SEND_ERROE = "kafka消息体 发送异常！";


    public static final String NULL_USER_NUM = "工号为空";
    public static final String NULL_USER_LOGIN_NAME = "登录名为空";
    public static final String NULL_USER_NAME = "用户名为空";
    public static final String NULL_USER_EMAIL = "用户邮箱为空";
    public static final String NULL_USER_PHONE = "用户手机号为空";
    public static final String NULL_USER_SEX = "用户性别为空";

    public static final String NOT_EXIST_USER_NUM = "不存在的工号";

    public static final String ERROR_PHONE = "手机号校验失败";

    public static final String ERROR_EMAIL = "邮箱校验失败";

    public static final String ERROR_REPEAT_PHONE = "手机号重复";

    public static final String ERROR_REPEAT_EMAIL = "邮箱重复";

    public static final String ERROR_REPEAT_LOGIN_NAME = "登录名重复";

    public static final String ERROR_USER_LOGIN_NAME_NUMERIC = "用户登录名不能纯数字";

    public static final String ERROR_USER_LOGIN_NAME_FORM = "登录名必须是20位以内字母或字母加数字组合";
}
