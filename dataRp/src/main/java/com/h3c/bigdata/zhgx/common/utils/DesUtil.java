package com.h3c.bigdata.zhgx.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @program: zhgx
 * @description: 加解密工具
 * @author: h17338
 * @create: 2018-08-10 17:36
 **/
public class DesUtil {

    /**
     * 日志记录.
     */
    private static final Logger log = LoggerFactory.getLogger(DesUtil.class);

    private final static String encoding = "utf-8";

    private final static String key = "h3c@zhgx";

    private final static String secretKey = "h3c_bigdata_zhgx_system_password";

    /**
     * 加密
     *
     * @param plainText
     * @return
     */
    public static String encode(String plainText) {
        Key deskey = null;
        byte[] encryptData = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            deskey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            encryptData = cipher.doFinal(plainText.getBytes(encoding));
            BASE64Encoder base64Encoder = new BASE64Encoder();
            return str2Hex(base64Encoder.encode(encryptData));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static String decode(String data) {
        try {
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher deCipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(key.getBytes());
            deCipher.init(Cipher.DECRYPT_MODE, desKey, ips);
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] pasByte;
            pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(hex2Str(data)));
            return new String(pasByte, "UTF-8");
        } catch (Exception e) {
            log.error("decode",e);
        }
        return "";
    }

    public static String hex2Str(String theHex) {
        char[] chars = theHex.toCharArray();
        int len = chars.length / 2;
        byte[] theByte = new byte[len];
        for (int i = 0; i < len; i++) {
            theByte[i] = Integer.decode("0X" + chars[i * 2] + chars[i * 2 + 1]).byteValue();
        }
        return new String(theByte);
    }

    public static String str2Hex(String theStr) {
        int tmp;
        String tmpStr;
        byte[] bytes = theStr.getBytes();
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            tmp = bytes[i];
            if (tmp < 0) {
                tmp += 256;
            }
            tmpStr = Integer.toHexString(tmp);
            if (tmpStr.length() == 1) {
                result.append('0');
            }
            result.append(tmpStr);
        }
        return result.toString();
    }

    public static String bytes2Str(byte[] bytes) {
        if (bytes == null) return null;

        StringBuilder s = new StringBuilder();

        for (byte b : bytes) {
            s.append(byte2str(b));
        }
        return s.toString();
    }

    public static char[] byte2str(byte b) {

        char[] chars = new char[2];

        chars[0] = byte2char((byte) ((b & 0xF0) >> 4));
        chars[1] = byte2char((byte) (b & 0x0F));
        return chars;
    }

    public static char byte2char(byte x) {
        char c;

        if (x >= 0x00 && x <= 0x09) {
            c = (char) (x + 48);
        } else {
            c = (char) (x + 55);
        }
        return c;
    }

    public static void main(String[] args) {

        String psd = "admin";
        System.out.print(encode(psd));
    }


}
