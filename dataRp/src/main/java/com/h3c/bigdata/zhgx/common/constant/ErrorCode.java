package com.h3c.bigdata.zhgx.common.constant;

import java.io.Serializable;

/**
 * 错误码定义.
 *
 * @author Mingchao.Ji
 * @version 1.0
 * @date 2018/4/23
 */
public class ErrorCode implements Serializable {

    /**
     * 私有构造器.
     */
    private ErrorCode(){
    }

    /**
     * serialVersionUID.
     */
    private static final Long serialVersionUID = 1L;

    /** 通用模块错误码定义10000-11000; */

    /**
     * 数据库操作异常.
     */
    public static final String DATA_OPERATE_EXCEPTION = "10001";

    /**
     * 页码参数有误.
     */
    public static final String QUERY_PAGE_PARAMETER_ERROR = "10101";
    /**
     * 排序参数有误.
     */
    public static final String QUERY_DIR_PARAMETER_ERROR = "10102";

    /**
     * 分页查询失败.
     */
    public static final String QUERY_DATA_FAILED = "10103";

    /**
     * 信息删除失败.
     */
    public static final String DELETE_DATA_FAILED = "10104";

    /**
     * 信息更新失败.
     */
    public static final String UPDATE_DATA_FAILED = "10105";

    /**
     * 保存失败.
     */
    public static final String SAVE_DATA_FAILED = "10106";

    /**
     * 参数为空.
     */
    public static final String PARAM_IS_NULL = "10107";

    /**
     * 查询失败.
     */
    public static final String QUERY_FAILD = "10108";


    /**
     * 数据服务引擎的错误代码：11001-12000
     */
    //收藏资源目录controller出现错误
    public static final String COLLECTION_ERROR = "11001";

    //收藏资源目录  参数错误
    public static final String PARAM_ERROR = "11002";

    //资源目录处于审批的锁定状态
    public static  final String DATA_CATALOG_STATUS = "11003";

    //订阅接口未签署保密协议，订阅失败
    public static final String NOT_SIGN_AGREEMENT = "11004";


    /**
     * 数据智能引擎错误代码：12001-13000
     */


    /**
     * 公共平台引擎错误代码：13001-14000
     */


    /**
     * 系统管理：14001-15000
     */
    //参数为空
    public static final String SYSTEM_PARAM_NULL_ERROR = "14001";

    //参数值已存在
    public static final String SYSTEM_PARAM_EXIST_ERROR = "14002";

    //参数值存在非法字符
    public static final String SYSTEM_PARAM_ILLEGAL_ERROR = "14003";

    //查询结果不存在
    public static final String SYSTEM_PARAM_NOT_EXIST_ERROR = "14004";


    // 文件管理相关错误码15-XXX
    // 没有文件
    public static final String NO_FILE = "15001";

    // 文件保存失败
    public static final String SAVE_FILE_SUCCESS = "15002";

    // 文件保存失败
    public static final String SAVE_FILE_FAILED = "15003";

    // 文件输出成功
    public static final String OUTPUT_FILE_SUCCESS = "15004";

    // 文件输出成功
    public static final String OUTPUT_FILE_FAILED = "15005";

    // 存储临时文件失败 (eg. MultipartFile.getInputStream() throws IOExcetion)
    public static final String SAVE_TEMP_FILE_FAILED = "15005";

}
