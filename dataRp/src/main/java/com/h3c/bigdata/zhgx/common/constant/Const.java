package com.h3c.bigdata.zhgx.common.constant;

/**
 * 常量工具类.
 *
 * @author Mingchao.Ji
 * @version 1.0
 * @date 2018/4/23
 */
public class Const {

    /**
     * 构造方法.
     */
    public Const() {
    }

    /**
     * 失败.
     */
    public final static String kCode_Fail = "0";

    /**
     * 成功.
     */
    public final static String kCode_Success = "1";


    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 自动去除表前缀
     */
    public static String AUTO_REOMVE_PRE = "true";

    /**
     * 当前记录起始索引
     */
    public static String PAGENUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static String PAGESIZE = "pageSize";

    /**
     * 排序列
     */
    public static String ORDERBYCOLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static String ISASC = "isAsc";


    /**
     * 照片压缩相关常量
     */
    public static String COMPRESSPREFIX = "img_";
    public static Integer MAXIMAGESIZE = 3;
    public static Integer IMAGEUNIT = 1048576;
    public static Integer COMPRESSWIDTH = 1280;
    public static Integer COMPRESSHEIGHT = 1024;
    public static float COMPRESSSCALE = 0.5f;

    /**
     * 菜单相关常量
     */
    public static String FUNCTIONMENUNUM = "2";
    public static String ADD = "add";
    public static String UPDATE = "update";
    public static String DELETE = "delete";

    /**
     * 角色相关变量
     */
    public static Integer DEPARTMENTROLEKEY = 2;
    public static Integer USERROLEKEY = 3;
    public static Integer SYSTEMADMINROLEKEY = 1;
    public static Integer SUPERADMINROLEKEY = 0;

    /**
     * 资源目录相关变量
     */
    public static String PUBLISHED_STATUS = "published";

}
