package com.h3c.bigdata.zhgx.function.report.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            throw new Exception();
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
                            if (vstr != null && !"".equals(vstr)) {
                                arrayStr[columnNum] = vstr;
                            }
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

                            if (arrayStr[columnNum] != "") {
                                map.put(arrayStr[columnNum], str);
                            }
                        }
                        //  这行不全是空格
                        if (totalBlankNum != firstRowLength) {
                            T t = doMapper(clazz, fieldMapper, map);
                            if (t != null) {
                                re.add(t);
                            }
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

    //生成excel文件
    public static void buildExcelFile(String filename, HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    public static String getSystemFileCharset(){
        Properties pro = System.getProperties();
        return pro.getProperty("file.encoding");
    }

    //获取cell值
    public static String formatCell(Cell cell){
        String ret;
        switch (cell.getCellType()) {
            case STRING:
                ret = cell.getStringCellValue();
                break;
            case FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = formatCell(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                            .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    ret = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil
                            .getJavaDate(value);
                    ret = sdf.format(date);
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
//                    ret = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BLANK:
                ret = "";
                break;
            case BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                ret = null;
                break;
            default:
                ret = null;
        }
        return ret; //有必要自行trim
    }


    /**
     * @param
     * @return
     * @Description: 获取有效行数
     * @Author: w15112
     * @Date: 2019/8/13 17:18
     */
    public static int getRealRow(Sheet sheet) {
        int total = 0;
        boolean flag = false;
        for (int i = 1; i <= sheet.getLastRowNum(); ) {
            Row r = sheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                //if (c.getCellType() != Cell.CELL_TYPE_BLANK && (c.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isNotBlank(c.getStringCellValue()))) {
                if (c.getCellType() != BLANK ) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {// 如果是空白行（即可能没有数据，但是有一定格式）
                if (i == sheet.getLastRowNum()) {
                    // 如果到了最后一行，直接将那一行remove掉
                    sheet.removeRow(r);
                } else {
                    // 如果还没到最后一行，则数据往上移一行
                    sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                }
            }
        }
        System.out.println("有效行数为:" + (sheet.getLastRowNum() + 1));
        total = sheet.getLastRowNum() + 1;
        return total;
    }
}
