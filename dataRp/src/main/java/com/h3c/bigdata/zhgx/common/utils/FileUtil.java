package com.h3c.bigdata.zhgx.common.utils;


import com.h3c.bigdata.zhgx.common.constant.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作工具类
 * @author c16944
 * @since 2018年4月26日
 * @version 1.0
 */
public class FileUtil {
    /**
     * 工具类不需要实例化
     */
    private FileUtil() {}

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     * @param path 待修复路径
     * @return 修复后的路径
     */
    public static String modifyPath(String path){
        String p = StringUtils.replace(path, "\\", "/");
        p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")){
            p += "/";
        }
        if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")){
            p = p + "/";
        }
        return p;
    }
    /**
     * 根据全路径名获取文件名
     * @param fullPathName 文件全路径
     * @return 文件名
     */
    public static String getFileName(String fullPathName){
        try{
            fullPathName = fullPathName.replaceAll("/", "\\\\");
            int fileIndex= fullPathName.lastIndexOf("\\")+1;
            return fullPathName.substring(fileIndex,fullPathName.length());
        }catch(Exception e){
            log.debug(e.getMessage());
            return "";
        }
    }

    /**
     * 根据全路径名获取目录
     * @param fullPathName 文件全路径
     * @return 文件所在目录
     */
    public static String getFilePath(String fullPathName){
        try{
            fullPathName = fullPathName.replaceAll("/", "\\\\");
            int fileIndex= fullPathName.lastIndexOf("\\");
            return fullPathName.substring(0,fileIndex);
        }catch(Exception e){
            return "";
        }
    }


    /**
     * 检查文件大小是否合法
     * @param file 文件
     * @param maxSize 允许文件最大值
     * @return 是否合法
     */
    public static boolean checkSize(MultipartFile file, long maxSize) {
        if (!file.isEmpty()) {
        long fileSize = file.getSize();
        return !(fileSize > maxSize) || (0 == maxSize);
        }
        return false;
    }

    /**
     * 检查上传的文件后缀是否合法
     * @param fileName 文件名称
     * @param allExts 所有允许的文件后缀，如：".jpg|.png|.git|.jpeg"，为空不做限制
     * @return 合法true,非法false
     */
    public static boolean checkExt(String fileName,String allExts){
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if(allExts.isEmpty()){
            return true;
        }else{
            return allExts.contains(suffixName);
        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件url
     * @param fileName 文件名称
     * @param response 文件下载返回
     * @throws IOException io异常
     */
    public  static void downLoadFile(String fileUrl,String fileName,HttpServletResponse response) throws IOException
    {
        File file = new File(fileUrl);
        InputStream inStream = new BufferedInputStream(new FileInputStream(file));//获取输入流

        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + file.length());
        //设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len;
        byte[] buff = new byte[4096];
        while((len=inStream.read(buff)) != -1){
            out.write(buff, 0, len);
        }
        while((len = inStream.read()) != -1){
            out.write(len);
        }
        inStream.close();
        out.flush();
        out.close();
    }

    /**
     * 根据文件路径确定文件编码方式
     * @param filePath 文件路径
     * @return 文件编码方式
     * @throws Exception io异常
     */
    public static String getFileEncoding(File filePath) throws Exception
    {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath));
        int p = (bin.read() << 8) + bin.read();
        String code ;
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }

    /**
    * 删除文件，可以删除单个文件或文件夹
    * @param fileName 文件名
    * @return 删除成功或文件不存在true，失败false
    */
    public static boolean delFileOrDir(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + "文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return FileUtil.deleteFile(fileName);
            } else {
                return FileUtil.deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     * @param fileName 文件名
     * @return 删除成功或文件不存在true，失败false
     */
    private static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     * 删除目录及目录下的文件
     * @param dirName 目录路径
     * @return 删除成功或目录不存在true，否则返回false
     */
    private static boolean deleteDirectory(String dirName) {
        String dirNames;
        dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    flag = FileUtil.deleteFile(file.getAbsolutePath());
                    // 如果删除子文件失败，则退出循环
                    if (!flag) {
                        break;
                    }
                } else if (file.isDirectory()) {
                    flag = FileUtil.deleteDirectory(file.getAbsolutePath());
                    // 如果删除子目录失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
            }
            if (!flag) {
                log.debug("删除目录 " + dirName + " 失败!");
                return false;
            }
        }

        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 创建单个文件
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {  // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }
        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    public static void fetchFileToResponse(HttpServletRequest request, HttpServletResponse response, String fileName, String fileExt, String filePath) {
        File file = new File(filePath);
        try (InputStream inStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            setHeader(request, response, fileName, fileExt);

            response.addHeader(HttpHeaders.CONTENT_LENGTH, "" + file.length());
            int len;
            byte[] buff = new byte[4096];

            while ((len = inStream.read(buff)) != -1) {
                out.write(buff, 0, len);
            }

            while ((len = inStream.read()) != -1) {
                out.write(len);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void setHeader(HttpServletRequest request, HttpServletResponse response, String fileName, String fileExt)
            throws UnsupportedEncodingException {
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
            fileName = new String((fileName + fileExt).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        //设置文件下载头
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");

        //设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
    }
//    /**
//     * 创建目录
//     * @param descDirName 目录名,包含路径
//     * @return 创建成功true，否则false
//     */
//    public static boolean createDirectory(String descDirName) {
//        String descDirNames = descDirName;
//        if (!descDirNames.endsWith(File.separator)) {
//            descDirNames = descDirNames + File.separator;
//        }
//        File descDir = new File(descDirNames);
//        if (descDir.exists()) {
//            log.debug("目录 " + descDirNames + " 已存在!");
//            return false;
//        }
//        if (descDir.mkdirs()) {
//            log.debug("目录 " + descDirNames + " 创建成功!");
//            return true;
//        } else {
//            log.debug("目录 " + descDirNames + " 创建失败!");
//            return false;
//        }
//
//    }

    /**
     * 创建目录 (modified)
     *
     * @param descDirName 目录名,包含路径
     *
     */
    public static void createDirectory(String descDirName) throws IOException {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (!descDir.exists()) {
            if (!descDir.mkdirs()) {
                throw new IOException(String.format("文件夹[%s]创建失败", descDirName));
            }
        }
    }

    /**
     * 把文件吸入到指定的路径中
     * @param in 输入流
     * @param abstractPath 绝对路径
     * @return 写入成功返回true，否则返回false;
     */
    public static boolean writeFile(InputStream in,String abstractPath,String fileName)
    {
        if(in == null || StringUtils.isEmpty(abstractPath))
        {
            log.error("输入数据流和绝对路径不能为空或null");
            return false;
        }
        if (!abstractPath.endsWith(File.separator)) {
            abstractPath = abstractPath + File.separator;
        }
        File descDir = new File(abstractPath);
        if (descDir.exists()) {
            log.info("目录 " + abstractPath + " 已存在!");
        }else if (descDir.mkdirs()) {
            log.info("目录 " + abstractPath + " 创建成功!");
        }else {
            log.error("目录 " + abstractPath + " 创建失败");
            return false;
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(abstractPath + fileName));
            byte[] temp = new byte[1024];
            int read = in.read(temp);
            while(read != -1)
            {
                out.write(temp,0,read);
                read = in.read(temp);
            }
            out.close();
        } catch (FileNotFoundException e) {
            log.error("文件创建错误",e);
            return false;
        } catch (IOException e) {
            log.error("文件创建错误",e);
            return false;
        }
        return true;
    }

    public enum FileType {
        /**
         * JPEG  (jpg)
         */
        JPEG("JPG", "FFD8FF"),

        /**
         * PNG
         */
        PNG("PNG", "89504E47"),

        /**
         * GIF
         */
        GIF("GIF", "47494638"),

        /**
         * TIFF (tif)
         */
        TIFF("TIF", "49492A00"),

        /**
         * Windows bitmap (bmp)
         */
        BMP("BMP", "424D"),

        BMP_16("BMP", "424D228C010000000000"), //16色位图(bmp)

        BMP_24("BMP", "424D8240090000000000"), //24位位图(bmp)

        BMP_256("BMP", "424D8E1B030000000000"), //256色位图(bmp)

        /**
         * CAD  (dwg)
         */
        DWG("DWG", "41433130"),

        /**
         * Adobe photoshop  (psd)
         */
        PSD("PSD", "38425053"),

        /**
         * Rich Text Format  (rtf)
         */
        RTF("RTF", "7B5C727466"),

        /**
         * XML
         */
        XML("XML", "3C3F786D6C"),

        /**
         * HTML (html)
         */
        HTML("HTML", "68746D6C3E"),

        /**
         * Email [thorough only] (eml)
         */
        EML("EML", "44656C69766572792D646174653A"),

        /**
         * Outlook Express (dbx)
         */
        DBX("DBX", "CFAD12FEC5FD746F "),

        /**
         * Outlook (pst)
         */
        PST("", "2142444E"),

        /**
         * doc;xls;dot;ppt;xla;ppa;pps;pot;msi;sdw;db
         */
        OLE2("OLE2", "0xD0CF11E0A1B11AE1"),

        /**
         * Microsoft Word/Excel 注意：word 和 excel的文件头一样
         */
        XLS("XLS", "D0CF11E0"),

        /**
         * Microsoft Word/Excel 注意：word 和 excel的文件头一样
         */
        DOC("DOC", "D0CF11E0"),

        /**
         * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样
         */
        DOCX("DOCX", "504B0304"),

        /**
         * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样 504B030414000600080000002100
         */
        XLSX("XLSX", "504B0304"),

        /**
         * Microsoft Access (mdb)
         */
        MDB("MDB", "5374616E64617264204A"),

        /**
         * Word Perfect (wpd)
         */
        WPB("WPB", "FF575043"),

        /**
         * Postscript
         */
        EPS("EPS", "252150532D41646F6265"),

        /**
         * Postscript
         */
        PS("PS", "252150532D41646F6265"),

        /**
         * Adobe Acrobat (pdf)
         */
        PDF("PDF", "255044462D312E"),

        /**
         * Quicken (qdf)
         */
        QDF("qdf", "AC9EBD8F"),

        /**
         * QuickBooks Backup (qdb)
         */
        QDB("qbb", "458600000600"),

        /**
         * Windows Password  (pwl)
         */
        PWL("PWL", "E3828596"),

        /**
         * ZIP Archive
         */
        ZIP("", "504B0304"),

        /**
         * ARAR Archive
         */
        RAR("", "52617221"),

        /**
         * WAVE (wav)
         */
        WAV("WAV", "57415645"),

        /**
         * AVI
         */
        AVI("AVI", "41564920"),

        /**
         * Real Audio (ram)
         */
        RAM("RAM", "2E7261FD"),

        /**
         * Real Media (rm) rmvb/rm相同
         */
        RM("RM", "2E524D46"),

        /**
         * Real Media (rm) rmvb/rm相同
         */
        RMVB("RMVB", "2E524D46000000120001"),

        /**
         * MPEG (mpg)
         */
        MPG("MPG", "000001BA"),

        /**
         * Quicktime  (mov)
         */
        MOV("MOV", "6D6F6F76"),

        /**
         * Windows Media (asf)
         */
        ASF("ASF", "3026B2758E66CF11"),

        /**
         * ARJ Archive
         */
        ARJ("ARJ", "60EA"),

        /**
         * MIDI (mid)
         */
        MID("MID", "4D546864"),

        /**
         * MP4
         */
        MP4("MP4", "00000020667479706D70"),

        /**
         * MP3
         */
        MP3("MP3", "49443303000000002176"),

        /**
         * FLV
         */
        FLV("FLV", "464C5601050000000900"),

        /**
         * 1F8B0800000000000000
         */
        GZ("GZ", "1F8B08"),

        /**
         * CSS
         */
        CSS("CSS", "48544D4C207B0D0A0942"),

        /**
         * JS
         */
        JS("JS", "696B2E71623D696B2E71"),

        /**
         * Visio
         */
        VSD("VSD", "d0cf11e0a1b11ae10000"),

        /**
         * WPS文字wps、表格et、演示dps都是一样的
         */
        WPS("WPS", "d0cf11e0a1b11ae10000"),

        /**
         * torrent
         */
        TORRENT("TORRENT", "6431303A637265617465"),

        /**
         * JSP Archive
         */
        JSP("JSP", "3C2540207061676520"),

        /**
         * JAVA Archive
         */
        JAVA("JAVA", "7061636B61676520"),

        /**
         * CLASS Archive
         */
        CLASS("CLASS", "CAFEBABE0000002E00"),

        /**
         * JAR Archive
         */
        JAR("JAR", "504B03040A000000"),

        /**
         * MF Archive
         */
        MF("MF", "4D616E69666573742D56"),

        /**
         * EXE Archive
         */
        EXE("EXE", "4D5A9000030000000400"),

        /**
         * ELF Executable
         */
        ELF("ELF", "7F454C4601010100"),

        /**
         * Lotus 123 v1
         */
        WK1("WK1", "2000604060"),

        /**
         * Lotus 123 v3
         */
        WK3("WK3", "00001A0000100400"),

        /**
         * Lotus 123 v5
         */
        WK4("WK4", "00001A0002100400"),

        /**
         * Lotus WordPro v9
         */
        LWP("LWP", "576F726450726F"),

        /**
         * Sage(sly.or.srt.or.slt;sly;srt;slt)
         */
        SLY("SLY", "53520100"),

        /**
         * CHM Archive
         */
        CHM("CHM", "49545346030000006000"),
        INI("INI", "235468697320636F6E66"),
        SQL("SQL", "494E5345525420494E54"),
        BAT("BAT", "406563686F206f66660D"),
        PROPERTIES("", "6C6F67346A2E726F6F74"),
        MXP("", "04000000010000001300"),

        NOT_EXITS_ENUM("", "");

        //文件类型对应的名称
        private String fileTypeName;

        //文件类型对应的魔数
        private String magicNumberCode;

        private FileType(String fileTypeName, String magicNumberCode) {
            this.fileTypeName = fileTypeName;
            this.magicNumberCode = magicNumberCode;
        }

        public String getFileTypeName() {
            return fileTypeName.toLowerCase();
        }

        public String getMagicNumberCode() {
            return magicNumberCode.toLowerCase();
        }


        /**
         * 根据文件类型获取文件类型魔数编码
         * 默认返回标准件
         *
         * @param magicNumberCode - 文件类型魔数编码
         *
         * @return
         */
        public static FileType getByMagicNumberCode(String magicNumberCode) {
            if (StringUtils.isNotBlank(magicNumberCode)) {
                for (FileType type : values()) {
                    if (magicNumberCode.toUpperCase().startsWith(type.getMagicNumberCode())) {
                        return type;
                    }
                }
            }

            return FileType.NOT_EXITS_ENUM;
        }

        /**
         * 根据文件类型后缀名获取枚举
         *
         * @param fileTypeName - 文件类型后缀名
         *
         * @return
         */
        public static FileType getByFileTypeName(String fileTypeName) {
            if (StringUtils.isNotBlank(fileTypeName)) {
                for (FileType type : values()) {
                    if (type.getFileTypeName().equalsIgnoreCase(fileTypeName)) {
                        return type;
                    }
                }
            }
            return FileType.NOT_EXITS_ENUM;
        }
    }

    public static void copy(String from, String to) throws IOException {
        File file = new File(getFilePath(to));
        boolean exist = true;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                exist = false;
            }
        }

        if (!exist) {
            throw new IOException("copy失败");
        }

        try (FileInputStream fis = new FileInputStream(from);
             FileOutputStream fos = new FileOutputStream(to)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = fis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        }
    }





}
