package com.h3c.bigdata.zhgx.common.exception;

/**
 * errorCode的补充说明 列举所有用到的errorCode
 *
 * @author w17193
 */
public enum ComErrorCode {
    /**
     * 系统级别的以0开头
     */
    SYS_ERROR_000000("000000", "系统异常", "请直接联系管理员"),
    SYS_SUCCESS_000001("000001", "{0}成功", ""),

    SYS_ERROR_000002("000002", "{0}为空", "请确认填写"),

    SYS_ERROR_000003("000003", "token超时失效", "请重新登录"),

    /**
     * 登录异常的以1开头
     */
    LOGIN_ERROR_100000("100000", "登录超时", "请重新登录！"),
    LOGIN_ERROR_100009("100009", "用户部门信息不存在", "请联系管理员！"),
    LOGIN_ERROR_100001("100001", "账号不存在！", "请重新登录！"),
    LOGIN_ERROR_100002("100002", "存在多条账号！", "请重新登录！"),
    LOGIN_ERROR_100003("100003", "账号已停用!", "请更换账号重试！"),
    LOGIN_ERROR_100004("100004", "用户密码数据不存在！", "请更换账号重试！"),
    LOGIN_ERROR_100005("100005", "用户密码数据有多条！", "请联系管理员，处理脏数据！"),
    LOGIN_ERROR_100006("100006", "无权限访问", "请确认该资源是否可以访问"),
    LOGIN_ERROR_100007("100007", "此账号已在另一个设备上登录", "如非本人，请尽快修改密码！"),
    LOGIN_ERROR_100008("100008", "用户名或密码错误", "请重新登录！");

    private String errorCode;
    private String errorMsg;
    private String msgDetail;

    ComErrorCode(String errorCode, String errorMsg, String msgDetail) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.msgDetail = msgDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }
}
