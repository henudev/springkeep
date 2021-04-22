package com.h3c.bigdata.zhgx.function.report.base;

/**
 * @author sunleilei
 * @since 2018-05-03
 */
public class PublicResultConstant {

    public static final String FAILED  = "系统错误";

    public static final String SUCCEED = "操作成功";

    public static final String UNAUTHORIZED  = "获取登录用户信息失败";

    public static final String ERROR  = "操作失败";

    public static final String DATA_ERROR  = "数据操作错误";

    public static final String PARAM_ERROR  = "参数错误";

    public static final String INVALID_USERNAME_PASSWORD  = "用户名或密码错误";

    public static final String INVALID_RE_PASSWORD  = "两次输入密码不一致";

    public static final String INVALID_USER  = "用户不存在";
    
    public static final String NULL_USER  = "角色名不能为空";

    public static final String NULL_TYPE  = "角色类型不能为空";

    public static final String INVALID_ROLE  = "角色不存在";
    
    public static final String HAVE_ROLE  = "角色名已存在";
    
    public static final String INSERT_ERROR_ROLE  = "新增角色失败";

    public static final String ROLE_USER_USED  = "角色使用中，不可删除";

    public static final String USER_NO_PERMITION  = "当前用户无该接口权限";

    public static final String VERIFY_PARAM_ERROR  = "校验码错误";

    public static final String VERIFY_PARAM_PASS  = "校验码过期";

    public static final String MOBILE_ERROR  = "手机号格式错误";

    public static final String UPDATE_ROLEINFO_ERROR  = "更新角色信息失败";

    public static final String UPDATE_SYSADMIN_INFO_ERROR  = "不能修改管理员信息!";
    /**
     * 模板编号不能为空.
     */
    public static final String TEMPLATE_NUM_IS_NULL = "数据库表名不能为空";

    public static final String TEMPLATE_NUM_REPETITION = "数据库表名重复";
    /**
     * 模板编号重复.
     */
    public static final String TEMPLATE_NAME_REPETITION = "模板名称重复";

    public static final String HAVE_DIC_NAME  = "字典名称已经存在";

    public static final String HAVE_DIC_NAME_IS_NULL  = "字典名称为空";

    public static final String HAVE_DIC_CODE  = "字典编码已经存在";

    public static final String HAVE_DIC_CODE_IS_NULL  = "字典编码为空";

    public static final String INSERT_ERROR_DIC  = "新增字典失败";

    public static final String DIC_NOT_EXIT  = "该字典类型不存在";

    public static final String DIC_IS_DEL  = "该字典类型已删除";
    public static final String EXCEL_ERROR  = "excel文件格式错误";
}
