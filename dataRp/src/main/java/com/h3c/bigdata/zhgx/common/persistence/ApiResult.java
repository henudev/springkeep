package com.h3c.bigdata.zhgx.common.persistence;

import com.h3c.bigdata.zhgx.common.constant.Const;

import java.io.Serializable;

/**
 * Rest接口返回结果集.
 * @param <T> Data的类型.
 *
 * @author Mingchao.Ji
 * @version 1.0
 * @date 2018/4/23
 */

public class ApiResult<T> implements Serializable {


    /**
     * 返回标识符.
     */
    private String code;

    /**
     * 返回信息.
     */
    private String message;

    /**
     * 返回操作数据结果集.
     */
    private T data;
    public ApiResult() {
    }

    /**
     * 构造函数.
     */
    public ApiResult(String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 操作成功.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> success(){
        return new ApiResult<>(Const.kCode_Success, "", null);
    }

    /**
     * 操作成功.
     * @param data 操作结果.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>(Const.kCode_Success, "", data);
    }

    /**
     * 操作成功.
     * @param message 返回信息.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> success(String message){
        return new ApiResult<>(Const.kCode_Success, message, null);
    }


    /**
     * 操作成功.
     * @param message 返回信息.
     * @param data 操作结果.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> success(String message, T data){
        return new ApiResult<>(Const.kCode_Success, message, data);
    }

    /**
     * 操作失败.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> fail(){
        return new ApiResult<>(Const.kCode_Fail, "", null);
    }

    /**
     * 操作失败.
     * @param message 返回信息.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> fail(String message){
        return new ApiResult<>(Const.kCode_Fail, message, null);
    }

    /**
     * 操作失败.
     * @param message 返回信息.
     * @param data 操作结果.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> fail(String message, T data){
        return new ApiResult<>(Const.kCode_Fail, message, data);
    }

    /**
     * 操作失败.
     * @param code 返回结果标识符：错误码.
     * @param message 返回信息.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> fail(String code, String message){
        return new ApiResult<>(code, message, null);
    }

    /**
     * 操作失败.
     * @param code 返回结果标识符：错误码.
     * @param message 返回信息.
     * @param data 操作结果.
     * @param <T> 泛型占位符.
     * @return 操作结果集.
     */
    public static <T> ApiResult<T> fail(String code, String message, T data){
        return new ApiResult<>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
