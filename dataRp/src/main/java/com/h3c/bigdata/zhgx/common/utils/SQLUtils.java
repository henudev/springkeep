package com.h3c.bigdata.zhgx.common.utils;


/**
 * SQL查询帮助工具类
 */
public class SQLUtils {

    /**
     * 将下划线转义
     */
    public static String transUnderline(String input) {
        if(input.contains("\\")){
            return input.replace("\\","\\\\");
        }else if(input.contains("_")){
            return input.replace("_", "\\_");
        }else{
            return input.replace("%", "\\%");
        }
    }


}
