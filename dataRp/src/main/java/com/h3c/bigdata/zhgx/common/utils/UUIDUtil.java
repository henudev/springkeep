package com.h3c.bigdata.zhgx.common.utils;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * UUIDUtil
 *
 * Author Mingchao.Ji
 * Date 2018/4/25
 * Version 1.0
 */
public class UUIDUtil {
    /**
     * 获取随机UUID（使用JDK生成）
     * @return UUID
     */
    public static String getUUID() {
        while (true) {
            long uuid = UUID.randomUUID().getMostSignificantBits();
            if (uuid > 0) {
                return uuid + "";
            }
        }
    }

    /**
     * 生成一个纯数字型的UUID。
     */
    public static String absNumUUID() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }
    
    /**
     * 生成一个带-的UUID,长度为36位.
     * @return
     */
    public static String get() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 生成时间（时分秒）组成的批次号
     * @return String
     */
    public static String createUUID(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		return sdf.format(System.currentTimeMillis());
    }

    public static void main(String[] args) {
		System.out.println( createUUID());
        System.out.println( createUUID());
        System.out.println( createUUID());
        System.out.println( getUUID());
        System.out.println( getUUID());
        System.out.println( getUUID());
	}
}
