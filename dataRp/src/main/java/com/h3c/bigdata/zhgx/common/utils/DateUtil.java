package com.h3c.bigdata.zhgx.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class DateUtil {

    public static void main(String args[])
    {
        Date date = getEndDayOfLastYear();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
        System.out.println(format.format(date));
    }

    /**
     * 获取某一日期的零点日期
     * @param cal 日历日期
     * @return 返回当天的零点日期
     */
    public static Date getDayBeginTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某一日期的最后1秒日期
     * @param cal 日历日期
     * @return 返回当天的零点日期
     */
    public static Date getDayEndTime(Calendar cal) {
        cal.add(Calendar.DATE,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MILLISECOND,-1);//加一天减去1毫秒
        return cal.getTime();
    }

    /**
     * 获取今年是哪一年
     * @return 年份
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     * @return 月份
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }
    /**
     * 获取当前时区时间的前一周至昨天的日期（年月日）
     *
     * @return
     */
    public static List<String> getNearWeekDate() {
        List<String> list = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        for(int i = -7;i < 0;i++){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,i);
            list.add(format.format(c.getTime()));
        }
        return list;
    }

    /**
     * 获取上周的开始时间
     * @return 返回上周的开始时间
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {//注意：星期的第一天是周日
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayBeginTime(cal);
    }

    /**
     * 获取上周的结束时间(周日最后1秒)
     * @return 返回上周最后1S日期
     */
    public static Date getEndDayOfLastWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return getDayEndTime(cal);
    }

    /**
     * 获取本周的开始时间
     * @return 返回上周的开始时间
     */
    public static Date getBeginDayOfThisWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {//注意：星期的第一天是周日
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayBeginTime(cal);
    }

    /**
     * 获取本周的结束时间(周日最后1秒)
     * @return 返回本周最后1S日期
     */
    public static Date getEndDayOfThisWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfThisWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return getDayEndTime(cal);
    }



    /**
     * 获取上月的开始时间
     * @return 上月开始时间
     */
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayBeginTime(calendar);
    }

    /**
     * 获取上月的结束时间
     * @return 上月结束时间
     */
    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar);
    }

    /**
     * 获取上一季度的开始日期
     * @return 返回上一季度的开始日期
     */
    public static Date getBeginDayOfLastSeason() {
        Date date = new Date();
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 6);
        cal.set(Calendar.DATE,1);
        return getDayBeginTime(cal);
    }

    /**
     * 获取上一季度的结束日期
     * @return 返回上一季度的结束日期
     */
    public static Date getEndDayOfLastSeason() {
        Date date = new Date();
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        cal.set(Calendar.DATE,1);
        cal.add(Calendar.DATE,-1);
        return getDayEndTime(cal);
    }

    /**
     * 获取上一年的开始日期
     * @return 返回上一年的开始日期
     */
    public static Date getBeginDayOfLastYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.YEAR,-1);
        return getDayBeginTime(cal);
    }

    /**
     * 获取上一年的结束日期
     * @return 返回上一年的结束日期
     */
    public static Date getEndDayOfLastYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        cal.add(Calendar.YEAR,-1);
        return getDayEndTime(cal);
    }

    /**
     * 根据数据类型获取时间周期开始时间
     * @return 返回时间周期的开始日期
     */
    public static Date getStartDayOfPeriod(String staType){
        Date startDayOfPeriod = null;
        if("0".equals(staType)){
            //类型0，表示周数据
            startDayOfPeriod = getBeginDayOfLastWeek();
        } else if ("1".equals(staType)){
            //类型1，表示月数据
            startDayOfPeriod = getBeginDayOfLastMonth();
        } else if("2".equals(staType)){
            //类型2，表示季度数据
            startDayOfPeriod = getBeginDayOfLastSeason();
        } else if("3".equals(staType)){
            //类型3，表示年数据
            startDayOfPeriod = getBeginDayOfLastYear();
        }
        return startDayOfPeriod;
    }


    /**
     * 根据数据类型获取时间周期结束时间
     * @return 返回时间周期的结束日期
     */
    public static Date getEndDayOfPeriod(String staType){
        Date EndDayOfPeriod = null;
        if("0".equals(staType)){
            //类型0，表示周数据
            EndDayOfPeriod = getEndDayOfLastWeek();
        } else if ("1".equals(staType)){
            //类型1，表示月数据
            EndDayOfPeriod = getEndDayOfLastMonth();
        } else if("2".equals(staType)){
            //类型2，表示季度数据
            EndDayOfPeriod = getEndDayOfLastSeason();
        } else if("3".equals(staType)){
            //类型3，表示年数据
            EndDayOfPeriod = getEndDayOfLastYear();
        }
        return EndDayOfPeriod;
    }

    /**
     * 获取当前时区的时间
     */
    public static Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getNowDate()
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static  String getStringTime(Date time){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringTime ="";
        if (time != null){
            stringTime = dateFormat.format(time);
        }
        return  stringTime;
    }

    public static Date getDateTime(String time)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static  String formatDate(String inputDate) {
        try {
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX", Locale.US);
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 获取当前时间：年月日
     * @Author: wuYaBing
     * @param
     * @return: null
     * @Date: 2020/5/15 15:04
     **/
    public static String getCurrentDate()
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
