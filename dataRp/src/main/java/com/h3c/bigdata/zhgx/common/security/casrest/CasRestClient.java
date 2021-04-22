package com.h3c.bigdata.zhgx.common.security.casrest;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

/**
 * @Description: 获取免密跳转公共资源引擎首页的服务票据
 * @author: y14079
 * @date: 2017-12-13
 * @modified by: h17338
 * @modified date: 2018-08-10
 * @problem no:
 */
public class CasRestClient {

    public static void main(String[] args) throws Exception {
        String username ="admin";
        String password ="admin@h3c";
        String casServerUrl = "http://100.5.14.80:30081/sso/tickets";//cas Server的地址
        String serviceUrl ="http://100.5.14.80:30085/core";//服务的地址

        validateFromCAS(username, password, casServerUrl, serviceUrl);
    }

    public static String validateFromCAS(String username, String password ,String casServerUrl, String serviceUrl) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(URLEncoder.encode("username","UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(username,"UTF-8"));
        sb.append("&");
        sb.append(URLEncoder.encode("password","UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(password,"UTF-8"));
        String content = sb.toString();
        System.out.println("send http content is:"+content);

        HttpURLConnection urlConn = HttpsClientWithoutCheckSSL.getHttpsURLConnection(casServerUrl, content, "POST");

        String tgt = urlConn.getHeaderField("location");
        int responseCode = urlConn.getResponseCode();
        System.out.println("请求tgt responseCode is:"+responseCode);
        System.out.println("请求tgt(getHeaderField(\"location\")) is:"+tgt);
        if(tgt != null && responseCode == 201){
            System.out.println("Tgt is : " + tgt.substring( tgt.lastIndexOf("/") +1));
            tgt = tgt.substring( tgt.lastIndexOf("/") +1);

            String requestSTurl = casServerUrl+ "/"+ tgt ;
            System.out.println("requestSTurl is "+requestSTurl);

            String encodedServiceURL = URLEncoder.encode("service","utf-8") +"=" + URLEncoder.encode(serviceUrl,"utf-8");
            System.out.println("Service url(is requestSTurl param) is :" + encodedServiceURL);

            String st = HttpsClientWithoutCheckSSL.sendHttps(requestSTurl, encodedServiceURL, "POST");
            System.out.println("request st string is :"+st);
            return st;
        }
        return null;
    }
}
