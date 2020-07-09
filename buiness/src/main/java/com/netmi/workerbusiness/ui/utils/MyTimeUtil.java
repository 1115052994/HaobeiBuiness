package com.netmi.workerbusiness.ui.utils;

import com.netmi.baselibrary.utils.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yangmu on 17/1/13.
 */

public class MyTimeUtil {
    private static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static boolean compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    //获取今天日期yyyy-MM-dd
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取最近七天日期yyyy-MM-dd
    public static ArrayList<String> getWeekDates() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 7; i++) {
            Date date = new Date(System.currentTimeMillis() - i * (1000 * 60 * 60 * 24));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            list.add(sdf.format(date));
        }
        return list;
    }

    //获取第7天的时间戳
    public static long getSevenTime(long time){
        Date date = new Date(time + 7 * (1000 * 60 * 60 * 24));
        long dateTime = date.getTime();
        return dateTime;
    }
    // 获取当前时间 yyyy-MM-dd HH:mm:ss
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String finishTime = formatter.format(curDate);
        return finishTime;
    }

    // 获取当前时间 yyyy-MM-dd HH:mm:ss
    public static String getCurrentTimeWithDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String finishTime = formatter.format(curDate);
        return finishTime;
    }

    // 获取当前时间 yyyy/MM/dd hh:mm AM/PM
    public static String getCurrentTimeWithAmPm() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String finishTime = formatter.format(curDate);
        String am_pm = Calendar.getInstance().getTime().getHours() >= 12 ? "PM" : "AM";
        return finishTime + " " + am_pm;
    }

    // 获取当前时间 上午下午 hh:mm
    public static String getCurrentTimeWithAmPm(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
//        Date curDate = new Date(date);
        String finishTime = formatter.format(date);
        String am_pm = Calendar.getInstance().getTime().getHours() > 12 ? "下午" : "上午";
        return am_pm + " " + finishTime;
    }

    //获取指定日期是星期几
    public static String getWeekDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
//        cal.setTimeInMillis(System.currentTimeMillis());
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //获取指定日期是星期几
    public static int getWeekDateNumber(Date dt) {
        Calendar cal = Calendar.getInstance();
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
//        cal.setTimeInMillis(System.currentTimeMillis());
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    //获取今天月份
    public static String getTodayMonth() {

        return getTodayDate().split("-")[1];
    }

    //转换时间格式
    public static String timeFormat(int time) {
        int second = time % 60;
        int buff = time / 60;
        int min = buff % 60;
        int hour = buff / 60;
        return "" + hour + "时" + min + "分" + second + "秒";
    }

    public static String getTimeWithDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getTimeWithMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }

    public static String getMonthAndYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public static String getTimeWithMin(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getTimeWithDay(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getTimeOnlyMin(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String getTimeHourMinSecond(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2;
        date2 = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);
        return calendar.getTime();
    }

    public static String getTimeWithDay(String date) {
        String[] time = date.split(" ")[0].split("-");
        return time[0] + "年" + time[1] + "月" + time[2] + "日";
    }

    public static String getStringTime(String data) {
        String[] day = data.split(" ")[0].split("-");
        String[] time = data.split(" ")[1].split(":");
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        if (year == Strings.toInt(day[0])) {
            return day[1] + "月" + day[2] + "日" + " " + time[0] + ":" + time[1];
        } else {
            return day[0] + "年" + day[1] + "月" + day[2] + "日" + " " + time[0] + ":" + time[1];
        }
    }

    public static String getStringTime2(String data) {
        String[] day = data.split(" ")[0].split("-");
        String[] time = data.split(" ")[1].split(":");
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        if (year == Strings.toInt(day[0])) {
            return day[1] + "月" + day[2] + "日" ;
        } else {
            return day[0] + "年" + day[1] + "月" + day[2] + "日";
        }
    }

    public static String getStringTime3(String data) {
        String[] day = data.split(" ")[0].split("-");
        return day[1] + "-" + day[2] ;

    }

    public static String getStringTime1(String data) {
        String[] day = data.split(" ")[0].split("-");
        String[] time = data.split(" ")[1].split(":");
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        if (year == Strings.toInt(day[0])) {
            return day[1] + "-" + day[2] + " " + time[0] + ":" + time[1];
        } else {
            return day[0] + "-" + day[1] + "-" + day[2] + " " + time[0] + ":" + time[1];
        }
    }

    public static String getStringTimePoint(String data) {
        String[] day = data.split(" ")[0].split("-");
        String[] time = data.split(" ")[1].split(":");
        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        if (year == Strings.toInt(day[0])) {
            return day[1] + "." + day[2] + " " + time[0] + ":" + time[1];
        } else {
            return day[0] + "." + day[1] + "." + day[2] + " " + time[0] + ":" + time[1];
        }
    }

    public static String getStringTimePoint1(String data) {
        String[] day = data.split(" ")[0].split("-");
        String[] time = data.split(" ")[1].split(":");
        return day[1] + "." + day[2] + " " + time[0] + ":" + time[1];
    }

    public static String getTimeOnlyMin(String date) {
        String[] time = date.split(" ")[1].split(":");
        return time[0] + ":" + time[1];
    }

    public static String getTimeMini(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm");
        return format.format(time);
    }

    /**
     * 判断是不是闰年
     */
    public static boolean checkLeapYear(int year) {
        boolean flag = false;
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
            flag = true;
        }
        return flag;
    }
}
