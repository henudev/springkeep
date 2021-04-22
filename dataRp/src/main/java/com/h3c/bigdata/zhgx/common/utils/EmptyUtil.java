package com.h3c.bigdata.zhgx.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @Description: 空值判断工具类
 * @Author: LXF
 * @Date: 2019/11/20 17:25
 */
public class EmptyUtil {

    public static boolean isEmpty(Object obj){
        if(null == obj){
            return true;
        }else if(obj instanceof String){
            return ((String)obj).trim().equals("") ? true : false;
        } else if(obj instanceof Collection){
            return ((Collection)obj).size() == 0 ? true : false;
        } else if(obj instanceof Map){
            return ((Map)obj).size() == 0 ? true : false;
        } else if(obj.getClass().isArray()){
            return ((Object[])obj).length == 0 ? true : false;
        }
        return false;

    }

    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }

    public static boolean isNull(Object obj){
        return null == obj ? true : false;
    }

    public static boolean isNotNull(Object obj){
        return !isNull(obj);
    }
}
