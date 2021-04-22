package com.h3c.bigdata.zhgx.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * 操作excel
 */

public class ExcelUtil<T> {

    public static <T> List<T> readExcel(String path, Class<T> clazz, Map<String, String> fieldMapper) throws Exception {
        return readExcel(path, 0, 0, clazz, fieldMapper);
    }

    /**
     * <p>方法名     :readExcel </p>
     * <p>方法描述: </p>
     * <p>逻辑描述: </p>
     *
     * @param path 文件路径
     * @param row  从第几行开始 ，默认为表头信息
     * @param num  从第几列开始
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(String path, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws Exception {
        String suffixName = path.substring(path.lastIndexOf(".") + 1);
        if (StringUtils.isEmpty(path)) {
            throw new Exception();///
        } else {
            if (!StringUtils.isEmpty(suffixName)) {
                if ("xls".equals(suffixName)) {
                    return readXls(path, row, num, clazz, fieldMapper);
                } else if ("xlsx".equals(suffixName)) {
                    return readXlsx(path, row, num, clazz, fieldMapper);
                }
            }
            return null;
        }

    }

    /**
     * @param fileName
     * @param is       输入流
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(String fileName, InputStream is, Class<T> clazz, Map<String, String> fieldMapper) throws Exception {
        return readExcel(fileName, is, 0, 0, clazz, fieldMapper);
    }

    /**
     * <p>方法名     :readExcel </p>
     * <p>方法描述: </p>
     * <p>逻辑描述: </p>
     *
     * @param fileName 文件名
     * @param is       输入流
     * @param row      从第几行开始，默认为表头信息
     * @param num      从第几列开始
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(String fileName, InputStream is, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws Exception {
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (StringUtils.isEmpty(fileName)) {
            return null;
        } else {
            if (!StringUtils.isEmpty(suffixName)) {
                if ("xls".equals(suffixName)) {
                    return readXlsFileInputStream(is, row, num, clazz, fieldMapper);
                } else if ("xlsx".equals(suffixName)) {
                    return readXlsxFileInputStream(is, row, num, clazz, fieldMapper);
                }
            }
            return null;

        }
    }

    private static <T> List<T> readXlsx(String path, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, ParseException {
        InputStream is = new FileInputStream(path);
        return readXlsxFileInputStream(is, row, num, clazz, fieldMapper);
    }

    private static <T> List<T> readXlsxFileInputStream(InputStream is, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, ParseException {
        List<T> re = new ArrayList<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Map<String, Object> map = null;
        //for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            re.addAll(readExcelSheet(xssfSheet, row, num, clazz, fieldMapper));
        //}
        return re;
    }

    private static <T> List<T> readXls(String path, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, ParseException {
        InputStream is = new FileInputStream(path);
        return readXlsFileInputStream(is, row, num, clazz, fieldMapper);
    }

    private static <T> List<T> readXlsFileInputStream(InputStream is, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, NotOLE2FileException, ParseException {
        List<T> re = new ArrayList<>();
        //使用POIFSFileSystem构造HSSFWorkbook
        POIFSFileSystem fs = new POIFSFileSystem(is);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fs);

        // Read the Sheet
        //for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {// sheet数目
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        re.addAll(readExcelSheet(hssfSheet, row, num, clazz, fieldMapper));
        //}
        return re;

    }

    private static <T> List<T> readExcelSheet(Sheet sheet, int row, int num, Class<T> clazz, Map<String, String> fieldMapper) throws IllegalAccessException, NoSuchFieldException, InstantiationException, ParseException {
        List<T> re = new ArrayList<>();
        if (sheet != null) {
            // 首行的长度
            int firstRowLength = 0;
            String[] arrayStr = null;
            int total = num != 0 && sheet.getLastRowNum() > num ? num : sheet.getLastRowNum();
            for (int rowNum = row; rowNum <= total; rowNum++) {// 行数
                Row _row = sheet.getRow(rowNum);
                if (_row != null) {
                    if (rowNum == row) {
                        arrayStr = new String[_row.getLastCellNum()]; // _row.getPhysicalNumberOfCells()
                        for (int columnNum = 0; columnNum < _row.getLastCellNum(); columnNum++) {
                            String vstr = getValue(_row.getCell(columnNum));
                            // 如果表头为空则舍弃
                            if (vstr != null && !"".equals(vstr))
                                arrayStr[columnNum] = vstr;
                        }
                        firstRowLength = arrayStr.length;
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        int totalBlankNum = 0; // 总的空格数
                        /*for (int columnNum = 0; columnNum < hssfRow.getPhysicalNumberOfCells(); columnNum++) {*/
                        // 此处为避免表头没有下面的数据多，或者是某个单元格为空时读取不完
                        for (int columnNum = 0; columnNum < firstRowLength; columnNum++) {
                            Cell value = _row.getCell(columnNum);
                            if (value == null || value.getCellType() == BLANK) {
                                totalBlankNum++;
                            }
                            String str = getValue(value);

                            if (arrayStr[columnNum] != "")
                                map.put(arrayStr[columnNum], str);
                        }
                        //  这行不全是空格
                        if (totalBlankNum != firstRowLength) {
                            T t = doMapper(clazz, fieldMapper, map);
                            if (t != null)
                                re.add(t);
                        }
                    }
                }
            }
        }
        return re;
    }

    /**
     * 获取单元格的值，全部转为String，若为null，则返回""
     *
     * @param cell
     * @return 单元格的内容
     */
    private static String getValue(Cell cell) {

        if (cell == null) {
            return ""; // MySQL数据库中存储null值其实是占用空间的
        }

        if (cell.getCellType() == BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == NUMERIC) {
            if(DateUtil.isCellDateFormatted(cell)){
                //用于转化为日期格式
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                return formater.format(cell.getDateCellValue());
            } else {
                Double d = cell.getNumericCellValue();
                Double tempD = d - d.intValue();
                if (tempD > 0) {
                    return d.toString();
                } else {
                    DecimalFormat dfs = new DecimalFormat("0");
                    return dfs.format(cell.getNumericCellValue());
                }
            }
        } else {

            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 根据字段的映射关系，利用反射，将map转为对象
     *
     * @param clazz  实体的class
     * @param fields 字段映射，key与data中的key保持一致， value为实体中的属性
     * @param data   需要映射的数据
     * @param <T>    泛型，这里用以将实体给不确定化
     * @return clazz.newInstance() 实体的一个对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> T doMapper(Class<T> clazz, Map<String, String> fields, Map<String, Object> data)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException, ParseException {
        // 获取实体的一个实例对象
        T instance = clazz.newInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int notMatchCount = 0;
        // 循环读取数据，数据中key与字段关系的key一致
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            // 获得实体中字段的名称
            String fieldName = fields.get(entry.getKey());
            if (fieldName == null || "".equals(fieldName)) {
                notMatchCount++;
                continue;
            }
            String value = entry.getValue().toString();
            // 利用反射，将值给设置进去
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            String name = field.getType().getSimpleName();
            if ("Date".equals(name)) {
                field.set(instance, format.parse(value));
            } else if ("Integer".equals(name) || "int".equals(name)) {
                field.set(instance, Integer.valueOf(value));
            } else if ("Double".equals(name) || "double".equals(name)) {
                field.set(instance, Double.valueOf(value));
            } else if ("Float".equals(name) || "float".equals(name)) {
                field.set(instance, Float.valueOf(value));
            } else if ("Long".equals(name) || "long".equals(name)) {
                field.set(instance, Long.valueOf(value));
            } else if ("Short".equals(name) || "short".equals(name)) {
                field.set(instance, Short.valueOf(value));
            } else if ("Boolean".equals(name) || "boolean".equals(name)) {
                field.set(instance, Boolean.valueOf(value));
            } else if ("Byte".equals(name) || "byte".equals(name)) {
                field.set(instance, Byte.valueOf(value));
            } else if ("Character".equals(name) || "char".equals(name)) {
                field.set(instance, value.charAt(0));
            } else {
                field.set(instance, value);
            }
        }
        // 完全不能匹配
        if (notMatchCount == data.size()) {
            instance = null;
        }
        return instance;
    }

    //浏览器下载excel2
    public static void buildExcelDocument2(HttpServletRequest request, String filename, Workbook
            workbook, HttpServletResponse response)
            throws Exception {
        FileUtil.setHeader(request, response, filename, "");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * @Description: 设置单元格样式与字体样式
     * @Author: wuYaBing
     * @param wb
     * @param fontSize 字体大小
     * @return: CellStyle
     * @Date: 2020/5/15 13:30
     **/
    public static CellStyle  setCellStyle(XSSFWorkbook wb ,short fontSize){
        //正文字体
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints(fontSize);
        font.setBold(true);
        font.setColor(HSSFFont.COLOR_NORMAL);

        //单元格样式，左右上下居中 边框
        CellStyle commonStyle = wb.createCellStyle();
        commonStyle.setFont(font);
        commonStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        commonStyle.setLocked(true);
        commonStyle.setWrapText(true);// 自动换行
        commonStyle.setBorderBottom(BorderStyle.THIN); //下边框
        commonStyle.setBorderLeft(BorderStyle.THIN);//左边框
        commonStyle.setBorderTop(BorderStyle.THIN);//上边框
        commonStyle.setBorderRight(BorderStyle.THIN);//右边框
        return commonStyle;
    }
}
