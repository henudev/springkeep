package com.h3c.bigdata.zhgx.function.report.util;

import com.alibaba.fastjson.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class FileUtil {

    @Value("${enKey}")
    String enKey;

    @Value("${net_url}")
    String net_url;

    public static String KEY;
    public static String NET_URL;
    @PostConstruct
    public void init(){
        KEY = this.enKey;
        NET_URL = this.net_url;
    }

    /**
     * 多文件上传
     * @param files
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws JSONException
     */
    public static List<HashMap<String, Object>> mutlUpload(MultipartFile[] files, String uploadPath)
            throws Exception {
        if(files.length > 0){
            String path = uploadPath;
            //定义文件
            File parent = new File(path);
            if(!parent.exists()) {
                parent.mkdirs();
            }
            //创建这个集合保存所有文件的信息
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            //循环多次上传多个文件
            for (MultipartFile file : files) {
                //创建map对象保存每一个文件的信息
                HashMap<String, Object> map = new HashMap<String,Object>();
                String oldName = file.getOriginalFilename();
                long size = file.getSize();

                //使用TmFileUtil文件上传工具获取文件的各种信息
                //优化文件大小
                String sizeString = getFileSize(size,"M");
                //获取文件后缀名
                String ext = getFileSuffix(oldName);
//                //随机重命名，10位时间字符串
                String newFileName = getFileNameWithoutSuffix(oldName) + UUIDUtil.getUUID() + ext;

                String url = uploadPath + newFileName;
                String url2 = uploadPath + "aes/" + newFileName;
                String url3 = uploadPath + "aes/decode/" + newFileName;


                file.transferTo(new File(parent, newFileName));
                //加密位置文件start
                String key = KEY;
                AESUtils.encryptFile(key, url, url2);
                AESUtils.decryptFile(key, url2, url3);
                //加密位置文件end

                //源文件MD5
                String md5 = Md5CaculateUtil.getMD5(new File(url));
                map.put("oldname",oldName);//文件原名称
                map.put("ext",ext);
                map.put("size",sizeString);
                map.put("name",newFileName);//文件新名称
                map.put("url",url2);
                map.put("md5", md5);
                map.put("net_url", NET_URL + newFileName);
                listMap.add(map);
            }
            //以json方式输出到页面
            return listMap;
        }else{
            return null;
        }
    }

    /**
     * 判断文件大小
     *
     * @param len
     *            文件长度
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    /**
     * 返回M格式
     * @param len
     * @param unit
     * @return
     */
    public static String getFileSize(Long len, String unit) {
        StringBuilder sb = new StringBuilder();
        double fileSize = 0;
        DecimalFormat df = new DecimalFormat( "0.000 ");
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
            sb.append(String.valueOf(df.format(fileSize))).append("B");
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
            sb.append(String.valueOf(df.format(fileSize))).append("K");
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
            sb.append(String.valueOf(df.format(fileSize))).append("M");
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
            sb.append(String.valueOf(df.format(fileSize))).append("G");
        }
        return sb.toString();
    }

    public static String getFileSuffix(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        return suffix;
    };

    public static String getFileNameWithoutSuffix(String fileName){
        String str = fileName.substring(0, fileName.lastIndexOf("."));
        return str;
    }

    /**
     * AES加密
     */

}
