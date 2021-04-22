package com.h3c.bigdata.zhgx.common.utils;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 19477
 * 对文件进行zip压缩
 */
public class CompressUtils {
    private static Logger logger = LoggerFactory.getLogger(CompressUtils.class);
    static final int BUFFER_SIZE=1024*2;
    public static void zip(String sourceFile, String outFile) {
        long begin = System.currentTimeMillis();
        logger.info("开始执行压缩文件：{},存放目录：{}", sourceFile, outFile);
        final File result = new File(outFile);
        createZipFile(sourceFile, result);
        long end = System.currentTimeMillis();
        logger.info("执行压缩文件完成：{},存放目录：{}，用时：{} ms", sourceFile, outFile, (end - begin));
    }
    static class CustomInputStreamSupplier implements InputStreamSupplier {
        private File currentFile;


        public CustomInputStreamSupplier(File currentFile) {
            this.currentFile = currentFile;
        }

        @Override
        public InputStream get() {
            try {
                return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws IOException {
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
        archiveEntry.setMethod(ZipEntry.DEFLATED);
        final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
        scatterSample.addEntry(archiveEntry, supp);
    }

    private static void compressCurrentDirectory(File dir, ScatterSample scatterSample) throws IOException {
        if (dir == null) {
            throw new IOException("源路径不能为空！");
        }
        String relativePath;
        if (dir.isFile()) {
            relativePath = dir.getName();
            addEntry(relativePath, dir, scatterSample);
            return;
        }
        // 空文件夹
        if (dir.listFiles().length == 0) {
            relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "").substring(1);
            addEntry(relativePath + File.separator, dir, scatterSample);
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                compressCurrentDirectory(f, scatterSample);
            } else {
                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");
                if (StringUtils.isNotBlank(relativePath) && relativePath.startsWith(File.separator)) {
                    relativePath = relativePath.substring(1) + File.separator;
                }
                addEntry(relativePath + f.getName(), f, scatterSample);
            }
        }
    }


    private static void createZipFile(final String rootPath, final File result) {
        ZipArchiveOutputStream zipArchiveOutputStream = null;
        try {
            File dstFolder = new File(result.getParent());
            if (!dstFolder.isDirectory()) {
                dstFolder.mkdirs();
            }
            File rootDir = new File(rootPath);
            final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());
            compressCurrentDirectory(rootDir, scatterSample);
            zipArchiveOutputStream = new ZipArchiveOutputStream(result);
            scatterSample.writeTo(zipArchiveOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (zipArchiveOutputStream != null) {
                try {
                    zipArchiveOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把zip文件解压到指定的文件夹
     * @param zipFilePath   zip文件路径, 如 "D:/test/aa.zip"
     * @param destDir   解压后的文件存放路径, 如"D:/test/"
     */
    public static void decompressZip(String zipFilePath,String destDir) throws IOException {
        if(!isEndsWithZip(zipFilePath)){
            throw new IOException("源文件必须为zip压缩文件");
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("开始执行解压缩文件：{},存放目录：{}", zipFilePath, destDir);
        File zipfile = new File(zipFilePath);
        if (StringUtils.isBlank(destDir)) {
            destDir = zipfile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir
                + File.separator;
        ZipArchiveInputStream is = null;
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(
                    new FileInputStream(zipfile), BUFFER_SIZE));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        //生成该文件的父级目录
                        File file = new File(destDir+entry.getName());
                        if (file.getParentFile() != null && !file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        os = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        logger.info("执行解压缩文件结束：{},存放目录：{}，用时：{} ms", zipFilePath, destDir,stopWatch.getTotalTimeMillis());
    }

    /**
     * 判断文件名是否以.zip为后缀
     * @param fileName        需要判断的文件名
     * @return 是zip文件返回true,否则返回false
     */
    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if(fileName != null && !"".equals(fileName.trim())) {
            if(fileName.endsWith(".ZIP")||fileName.endsWith(".zip")){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 文件压缩
     * @param sourceFileName
     * @param zipFileName
     * @throws Exception
     */
    public static void zipCompress(String sourceFileName,String zipFileName) throws Exception
    {
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            //创建zip输出流
            out = new ZipOutputStream( new FileOutputStream(zipFileName));
            //创建缓冲输出流
            bos = new BufferedOutputStream(out);
            File sourceFile = new File(sourceFileName);
            //调用函数
            compress(out,bos,sourceFile,sourceFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(bos!=null){
                bos.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }

    private static void compress(ZipOutputStream out,BufferedOutputStream bos,File sourceFile,String base) throws Exception
    {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory())
        {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();

            if(flist.length==0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                out.putNextEntry(  new ZipEntry(base+"/") );
            }
            else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for(int i=0;i<flist.length;i++)
                {
                    compress(out,bos,flist[i],base+"/"+flist[i].getName());
                }
            }
        }
        else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {

            FileInputStream fos = null;
            BufferedInputStream bis = null;
            try {
                out.putNextEntry( new ZipEntry(base) );
                fos = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fos);

                int tag;
                while((tag=bis.read())!=-1)
                {
                    out.write(tag);
                }
                bis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bis!=null){
                    bis.close();
                }
                if(fos!=null){
                    fos.close();
                }
            }
        }
    }

}
