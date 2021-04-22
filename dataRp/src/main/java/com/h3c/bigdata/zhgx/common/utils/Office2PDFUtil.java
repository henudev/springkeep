    package com.h3c.bigdata.zhgx.common.utils;

    import com.aspose.cells.Workbook;
    import com.aspose.words.*;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.core.io.ClassPathResource;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.InputStream;
    import java.util.Iterator;

    /**
     * @ClassName Office2PDFUtil
     * @Description 转换为PDF的工具类
     * @Author l17561
     * @Date 2018/9/13 9:03
     * @Version V1.0
     */
    public class Office2PDFUtil {

        private static Logger logger = LoggerFactory.getLogger(Office2PDFUtil.class);

        private static String[] TYPES = new String[]{"WORD", "EXCEL"};

        public static void main(String[] args) throws Exception {
            doc2pdf("E:\\H3C\\攀枝花项目\\《政务资源目录系统》操作手册.docx", "d:\\eee.pdf", 1);
            //excel2pdf("d:/项目信息0620.xls","d:/eee.pdf");
        }

        public static void sss(String dir) throws Exception {
            // load source & destination documents
            Document srcDoc = new Document(dir + "source.docx");
            Document dstDoc = new Document(dir + "destination.docx");


            // set the appended document to start from a new page
            srcDoc.getFirstSection()
                    .getPageSetup()
                    .setSectionStart(SectionStart.NEW_PAGE);
            srcDoc.save(dir + "output2.docx");
            // append the source document using its original styles
            dstDoc.appendDocument(srcDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);
            // save final result
            dstDoc.save(dir + "output.docx");
        }

        public static void doc2pdf(String docPath, String pdfPath, int sections) throws Exception {
            if (!getLicense(TYPES[0])) {
                return; // 验证License 若不验证则转化出的pdf文档会有水印产生
            }

            long startTimestamp = System.currentTimeMillis();

            File pdf = new File(pdfPath);
            if (!pdf.exists()) {
                pdf.createNewFile();
            }

            Document wordDoc = new Document(docPath);
            Document pdfDoc = new Document(pdfPath);

            Iterator<Section> sectionIterator = wordDoc.getSections().iterator();
            if (sectionIterator.hasNext()){
                sectionIterator.next();
            }

            while (sectionIterator.hasNext()) {
                sectionIterator.remove();
            }

            pdfDoc.appendDocument(wordDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);
            pdfDoc.save(pdfPath, SaveFormat.PDF);
            long endTimestamp = System.currentTimeMillis();
            logger.info("transfer cost: " + (endTimestamp - startTimestamp));
        }

        public static void doc2pdf(String docPath, String pdfPath) {
            if (!getLicense(TYPES[0])) {
                return;
            }
            try {
                long old = System.currentTimeMillis();
                File file = new File(pdfPath); // 新建一个空白pdf文档
                FileOutputStream os = new FileOutputStream(file);
                Document doc = new Document(docPath);
                doc.save(os, SaveFormat.PDF);
                logger.info("共耗时：" + ((System.currentTimeMillis() - old) / 1000.0) + "秒");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("doc转pdf失败",e);
            }
        }

        public static void excel2pdf(String excelPath, String pdfPath) {
            if (!getLicense(TYPES[1])) {
                return;
            }
            try {
                long old = System.currentTimeMillis();
                Workbook wb = new Workbook(excelPath);// 原始excel路径
                FileOutputStream fileOS = new FileOutputStream(new File(pdfPath));
                wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
                fileOS.close();

                logger.info("共耗时：" + ((System.currentTimeMillis() - old) / 1000.0) + "秒");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 读取license.xml文件，其中license.xml放在resources路径下
         *
         * @return license是否有误
         */
        private static boolean getLicense(String type) {
            boolean result = true;
            try {
                // license.xml放在resources路径下
                // InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:license.xml")); 该方法导致Spring扫面不到打包在jar里面的文件
                InputStream is = new ClassPathResource("license.xml").getInputStream();
                switch (type) {
                    case "WORD":
                        com.aspose.words.License word = new com.aspose.words.License();
                        word.setLicense(is);
                        break;
                    case "EXCEL":
                        com.aspose.cells.License excel = new com.aspose.cells.License();
                        excel.setLicense(is);
                        break;
                    default:
                        result = false;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        }

    }
