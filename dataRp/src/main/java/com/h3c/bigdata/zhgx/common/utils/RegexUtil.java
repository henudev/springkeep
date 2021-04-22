package com.h3c.bigdata.zhgx.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Author:f13979
 * @Description: 通过正则表达式校验输入值
 * @Date:Created in 10:57 18/8/13
 * @Modified by:
 */
public class RegexUtil {

    //中国手机号正则表达式
    private static final String CHINA_PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
    //香港手机号
    private static final String HKPHONE_REGEX = "^(5|6|8|9)\\d{7}$";
    //邮箱
    private static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    //url
    private static final String URL_REGEX = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
    //固定电话
    private static final String FIXED_PHONE= "^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$";
    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str) || isFixedPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     *
     * 移动号段：139 138 137 136 135 134 147 150 151 152 157 158 159 178 182 183 184 187 188 198

     联通号段：130 131 132 155 156 166 185 186 145 176

     电信号段：133 153 177 173 180 181 189 199

     虚拟运营商号段：170 171
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        Pattern p = Pattern.compile(CHINA_PHONE_REGEX);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        Pattern p = Pattern.compile(HKPHONE_REGEX);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是固话
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isFixedPhoneLegal(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(FIXED_PHONE);
        Matcher m= p.matcher(str);
        return m.matches();
    }
    /**
     * 检查email是否正确
     * @param email
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isEmailLegal(String email) throws PatternSyntaxException
    {
        Pattern p = Pattern.compile(EMAIL_REGEX);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断url
     * @param url
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isUrlLegal(String url) throws PatternSyntaxException
    {
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(url);
        return m.matches();
    }

    public static void main(String[] args)
    {
        System.out.println(RegexUtil.isPhoneLegal("15515962654"));
    }

}
