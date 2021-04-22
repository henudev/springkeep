package com.h3c.bigdata.zhgx.common.log;

import com.h3c.bigdata.zhgx.common.constant.Const;
import com.h3c.bigdata.zhgx.common.spring.SpringUtils;
import com.h3c.bigdata.zhgx.function.system.entity.LoginLog;
import com.h3c.bigdata.zhgx.function.system.serviceImpl.LoginLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记录用户登录日志信息.
 *
 * @Author J16898
 * @Date 2018/7/31
 * @Version 1.0
 */
public class SystemLogUtils {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录格式 [ip][用户名][操作][错误消息]
     * <p/>
     * 注意操作如下： loginError 登录失败 loginSuccess 登录成功 passwordError 密码错误 changePassword 修改密码 changeStatus 修改状态
     *
     * @param userName
     * @param status
     * @param msg
     * @param args
     */
    public static void log(String userName, String status, String msg, Object... args ){
        StringBuilder s = new StringBuilder();
        s.append(getBlock(userName));
        s.append(getBlock(status));
        s.append(getBlock(msg));

        sys_user_logger.info(s.toString(), args);

        if (Const.LOGIN_SUCCESS.equals(status) || Const.LOGOUT.equals(status))
        {
            saveOpLog(userName, msg, Const.SUCCESS);
        }
        else if (Const.LOGIN_FAIL.equals(status))
        {
            saveOpLog(userName, msg, Const.FAIL);
        }
    }

    //保存用户登录日志到数据库
    public static void saveOpLog(String username, String message, String status)
    {
        LoginLogServiceImpl loginLogService = SpringUtils.getBean(LoginLogServiceImpl.class);
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginName(username);
        loginLog.setStatus(status);
        loginLog.setMsg(message);
        loginLogService.insertLoginLog(loginLog);
    }

    //日志参数格式转换
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
