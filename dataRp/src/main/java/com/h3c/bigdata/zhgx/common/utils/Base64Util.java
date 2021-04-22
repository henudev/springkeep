package com.h3c.bigdata.zhgx.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;

/**
 * @Author:f13979
 * @Description: Base64的util类
 * @Date:Created in 14:20 18/8/15
 * @Modified by:
 */
public class Base64Util {

    /**
     * 图片转base64
     * @param imgPath 图片的路径
     * @return
     * @throws Exception
     */
    public static String image2Base64(String imgPath) throws Exception {
        //检查参数
        if(StringUtils.isEmpty(imgPath))
            throw new Exception("参数错误");

        //检查文件是否存在
        File img = new File(imgPath);
        if (!img.exists())
            throw new Exception("图片不存在");

        //把文件专场base64
        FileInputStream in = new FileInputStream(img);
        byte[] b = new byte[in.available()];
        in.read(b);
        in.close();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }

    /**
     * base64转图片
     * @param base64 base64的字符串
     * @param imgPath 图片的路径
     * @param fileName 图片的文件名称
     * @return 返回图片路径
     * @throws Exception
     */
    public static String base642Img(String base64,String imgPath,String fileName) throws Exception {
        //检查参数
        if(StringUtils.isEmpty(base64) || StringUtils.isEmpty(imgPath) || StringUtils.isEmpty(fileName))
            throw new Exception("参数错误");
        //检查文件路径是否存在，不存在则创建
        File path = new File(imgPath);
        if(!path.exists()) {
            boolean result = path.mkdirs();
            if(!result)
                throw new Exception("文件夹创建失败");
        }
        if(!imgPath.endsWith(File.separator))
            imgPath += File.separator;

        //base64转码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(base64);

        for (int i = 0; i < b.length; ++i)
        {
            if(b[i] < 0)
                b[i] += 256;
        }

        //写文件
        FileOutputStream out = new FileOutputStream(imgPath + fileName);
        out.write(b);
        out.flush();
        out.close();

        return imgPath + fileName;
    }
    /**
     * 解码并解压字符串
     *
     * @param inStr
     * @return
     */

    public static String decodeBase64ZippedString(String inStr) {
        String outStr="";
        if (inStr == null || inStr.isEmpty()) {
            return "";
        }
        StringWriter writer = new StringWriter();
        // Base64解码
        byte[] bytes64 = Base64.decodeBase64(inStr.getBytes());

        // 解压
        ByteArrayInputStream zip = new ByteArrayInputStream(bytes64);
        GZIPInputStream unzip = null;
        InputStreamReader reader = null;
        BufferedInputStream in = null;
        try {
            unzip = new GZIPInputStream(zip, 8192);
            in = new BufferedInputStream(unzip, 8192);

            reader = new InputStreamReader(in, "utf-8");
            writer = new StringWriter();

            char[] buff = new char[8192];
            for (int length = 0; (length = reader.read(buff)) > 0;) {
                writer.write(buff, 0, length);
            }

        } catch (Exception e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (unzip != null) {
                try {
                    unzip.close();
                } catch (IOException e) {
                }
            }
        }
        outStr = writer.toString();

        return outStr;
    }

    public static void main(String[] args) throws Exception {
        //String base64 = Base64Util.image2Base64("C:\\Users\\f13979\\Desktop\\index.png");
        //String base64 = "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";
        //System.out.println(Base64Util.base642Img(base64,"C:\\Users\\f13979\\Desktop","ceshi.png"));
    }
}
