package com.h3c.bigdata.zhgx.common.utils;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    /**{\"name\":\"平头哥\",\"age\":\"28\"}"
     　　* 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     　　* @param params 需要排序并参与字符拼接的参数组
     　　* @return 拼接后字符串"
     　　*/
    public  static String createLinkStringByGet(String ss)  {
        if(StringUtils.isEmpty(ss)){
            return "";
        }else{
            JSONObject jasonObject = JSONObject.parseObject(ss);
            Map<String,String> map = (Map)jasonObject;
            List<String> keys = new ArrayList<String>(map.keySet());
            Collections.sort(keys);
            String prestr = "";
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                Object value = map.get(key);
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
            return "?"+prestr;
        }

    }

    /**
     * dom4J处理String格式
     * @param str
     * @return
     * @throws Exception
     */
    public  static String formatXml(String str) throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(str);
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        // 将document写入到输出流
        xmlWriter.write(document);
        xmlWriter.close();
        return writer.toString();
    }

    /**
     *  xml格式的字符串转换为JsonObject
     * @param xmlStr xml格式的字符串
     * @return json对象
     */
    public static net.sf.json.JSONObject xmlStrParse2JsonObject(String xmlStr) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        String xml = xmlSerializer.read(xmlStr).toString();
        return net.sf.json.JSONObject.fromObject(xml);
    }
}
