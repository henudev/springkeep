package com.h3c.bigdata.zhgx.ws;

import com.h3c.bigdata.gop.websocket.core.session.BasicMessageStrategy;
import com.h3c.bigdata.gop.websocket.core.session.BusinessMessage;

/**
 * {@code LoginMessages}
 *
 * @author f18467
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginMessages implements BasicMessageStrategy {

    static BusinessMessage logout() {
        return new BusinessMessage(10003, true, true,"退出成功！");
    }

    static BusinessMessage tokenExpired() {
        return new BusinessMessage(10005, true, true,"Token 失效！");
    }

    @Override
    public BusinessMessage onUserSessionConflict(String user, String older, String newer) {
        return new BusinessMessage(10002, true, "您的账户 " + user + " 在其它浏览器登录");
    }

    @Override
    public BusinessMessage onUserIpConflict(String user, String last, String now) {
        return new BusinessMessage(10001, true, "警告！您的账户 " + user + " 在异地登录 " + now);
    }

    @Override
    public BusinessMessage onConnect(String user, String remote) {
        return new BusinessMessage(10000, false, "登陆成功");
    }

    @Override
    public BusinessMessage onReconnect(String user, String remote) {
        return new BusinessMessage(10004, false, user + ", 欢迎回来！");
    }

    @Override
    public BusinessMessage onLogoutSuccess(String user) {
        return logout();
    }

}
