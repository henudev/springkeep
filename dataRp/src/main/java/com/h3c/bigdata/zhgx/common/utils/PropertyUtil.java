package com.h3c.bigdata.zhgx.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件内容.
 *
 * @Author Mingchao.Ji
 * @Date 2018/4/27
 * @Version 1.0
 */
public class PropertyUtil {

    /**
     * logger.
     */
    public static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static final String PROPERTY_FILE = "zhgx.properties";

    /**
     * 获取${PROPERTY_FILE}里面的key对应的内容
     *
     * @param key key.
     * @return value.
     */
    public static String getValue(String key) {
        return getValue(PROPERTY_FILE, key, null);
    }

    /**
     * 获取资源文件里面的key对应的内容
     *
     * @param propertiesFilePath 配置文件路径.
     * @param key 关键字key.
     * @return value结果值.
     */
    public static String getValue(String propertiesFilePath, String key) {
        return getValue(propertiesFilePath, key, null);
    }

    public static Map<String, String> getValues() {
        return getValues(PROPERTY_FILE, null);
    }

    /**
     * @param propertiesFilePath 配置文件路径.
     * @return value结果值.
     */
    public static Map<String, String> getValues(String propertiesFilePath) {
        return getValues(propertiesFilePath, null);
    }

    /**
     * 获取资源文件里面的key对应的内容
     *
     * @param propertiesFilePath 配置文件路径.
     * @param key 关键字.
     * @return value结果值.
     */
    public static String getValue(String propertiesFilePath, String key, String encode) {
        String value = null;
        try {
            Properties pros = getProperties(propertiesFilePath, encode);
            value = pros.getProperty(key);
        } catch (IOException e) {
            logger.error("PropertyUtil.getValue(String propertiesFilePath:" + propertiesFilePath + ",String key:" + key
                    + ") exception ,return null", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取资源文件里面的key对应的内容
     *
     * @param propertiesFilePath 配置文件路径.
     * @return value结果值.
     */
    public static Map<String, String> getValues(String propertiesFilePath, String encode) {
        Map<String, String> map = new HashMap<>();
        try {
            Properties pros = getProperties(propertiesFilePath, encode);
            Enumeration<?> enumeration = pros.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = pros.getProperty(key);
                map.put(key, value);
            }
        } catch (IOException e) {
            logger.error("PropertyUtil.getValue(String propertiesFilePath:" + propertiesFilePath
                    + ") exception ,return null", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static Properties getProperties(String propertiesFilePath, String encode)
            throws Exception {
        Properties pros = new Properties();
        InputStream in = PropertyUtil.class.getResourceAsStream("/" + propertiesFilePath);
        if (StringUtils.isNotEmpty(encode)) {
            pros.load(new InputStreamReader(in, encode));
        } else {
            pros.load(in);
        }
        in.close();
        return pros;
    }
}
