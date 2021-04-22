package com.h3c.bigdata.zhgx.common.utils;

import com.h3c.bigdata.zhgx.common.constant.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 多文件打包下载
 */
@Slf4j
public class IOUtil {
    /**
     * get File Object
     */
    public static File getFile(String path, String fileName) throws IOException{
        File file;
        if (path != null && !path.equals("")) {
            file = new File(path, fileName);
        }else {
            file = new File(fileName);
        }
        return file;
    }

    /**
     * 获得指定文件的输出流
     * @param file
     * @return
     * @throws IOException
     */
    public static FileOutputStream getFileStream(File file) throws IOException {
        return new FileOutputStream(file);
    }

    /**
     * 将多个文件压缩
     * @param fileList 待压缩的文件列表
     * @param zipFileName 压缩文件名
     * @return 返回压缩好的文件
     * @throws IOException
     */
    public static File getZipFile(List<File> fileList, String path, String zipFileName) throws IOException {
        File zipFile = getFile(path, zipFileName);
        // 文件输出流
        FileOutputStream outputStream = getFileStream(zipFile);
        // 压缩流
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        int size = fileList.size();
        // 压缩列表中的文件
        for (int i = 0;i < size;i++) {
            File file = fileList.get(i);

            zipFile(file, zipOutputStream);
        }
        // 关闭压缩流、文件流
        zipOutputStream.close();
        outputStream.close();
        return zipFile;
    }


    /**
     * 将文件数据写入文件压缩流
     * @param file 带压缩文件
     * @param zipOutputStream 压缩文件流
     * @throws IOException
     */
    private static void zipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ZipEntry entry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(entry);

                final int MAX_BYTE = 10 * 1024 * 1024; // 最大流为10MB
                long streamTotal = 0; // 接收流的容量
                int streamNum = 0; // 需要分开的流数目
                int leaveByte = 0; // 文件剩下的字符数
                byte[] buffer; // byte数据接受文件的数据

                streamTotal = bis.available(); // 获取流的最大字符数
                streamNum = (int) Math.floor(streamTotal / MAX_BYTE);
                leaveByte = (int) (streamTotal % MAX_BYTE);

                if (streamNum > 0) {
                    for (int i = 0;i < streamNum;i++) {
                        buffer = new byte[MAX_BYTE];
                        bis.read(buffer, 0, MAX_BYTE);
                        zipOutputStream.write(buffer, 0, MAX_BYTE);
                    }
                }

                // 写入剩下的流数据
                buffer = new byte[leaveByte];
                bis.read(buffer, 0, leaveByte); // 读入流
                zipOutputStream.write(buffer, 0, leaveByte); // 写入流
                zipOutputStream.closeEntry(); // 关闭当前的zip entry

                // 关闭输入流
                bis.close();
                fis.close();
            }
        }
    }


    public static void fetchFileToResponse(HttpServletRequest request, HttpServletResponse response, String fileName, String fileExt, File file) throws IOException {
        fileExt='.'+fileExt;
        try (InputStream inStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            //根据不同浏览器判断不同的方式
            String header = request.getHeader(HttpHeaders.USER_AGENT).toUpperCase();

            if (HttpHeaders.UserAgent.isFirefox(header)) {
                //火狐也包含RV
                fileName = new String((fileName + fileExt).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else if (HttpHeaders.UserAgent.isIE(header)) {
                // IE浏览器
                fileName = java.net.URLEncoder.encode((fileName + fileExt), StandardCharsets.UTF_8.name());
            } else {
                // 谷歌、火狐等
                log.info("before::::::::::"+ fileName);
//                fileName = new String((fileName +fileExt).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                log.info("after::::::::::"+ fileName);
            }

            //设置文件下载头
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" +  URLEncoder.encode(fileName +fileExt, "UTF-8") + "\"");
            response.addHeader(HttpHeaders.CONTENT_LENGTH, "" + file.length());
            //设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            response.setHeader("filename",  URLEncoder.encode(fileName +fileExt, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "filename");

            int len;
            byte[] buff = new byte[4096];

            while ((len = inStream.read(buff)) != -1) {
                out.write(buff, 0, len);
            }

            while ((len = inStream.read()) != -1) {
                out.write(len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException("没有找到该文件");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("文件下载异常");
        }
    }
}
