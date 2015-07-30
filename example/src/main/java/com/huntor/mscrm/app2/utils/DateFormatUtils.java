package com.huntor.mscrm.app2.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.huntor.mscrm.app2.R;


/**
 * 日期格式化的工具类
 */
public class DateFormatUtils {

    // 一分钟的毫秒值
    public static final long ONE_MINUTE = 60 * 1000;

    // 一小时的毫秒值
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    // 一天的毫秒值
    public static final long ONE_DAY = 24 * ONE_HOUR;

    //7天的毫秒值
    public static final long SEVEN_DAY = 7*ONE_DAY;
    // 一月的毫秒值
    public static final long ONE_MONTH = 30 * ONE_DAY;

    // 一年的毫秒值
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    // 年-月-日 时:分:秒(2014-4-16 19:14:51)
    public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";
    // 年-月(2014-4)
    public static final String PATTERN_YEAR_MONTH = "yyyy-M";

    // 年月日时分秒
    //public static final String PATTERN_STAMP = "yyyyMMddHHmmss";

    //年月日时分秒的另一种格式
    public static final String PATTERN_STAMP = "yyyy-MM-dd HH:mm:ss";

    // 年.月.日
    public static final String PATTERN_DOT_TIME = "yyyy.MM.dd";

    public static final String PATTERN_TIME = "yyyyMMdd";
    
    public static final String PATTERN_BAR_TIME = "yyyy-MM-dd";
    
    public static final String PATTERN_SLASH_TIME = "yyyy/MM/dd";

    public static final String PATTERN_H_S = "HH:mm";
    /**
     * 获取时间戳
     * 
     * @return 时间戳(yyyyMMddHHmmss)
     */
    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_STAMP, Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 获取时间戳
     * 
     * @return 时间戳(yyyy.MM.dd)
     */
    public static String getDotTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DOT_TIME, Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 获取时间戳
     * 
     * @return 时间戳(yyyyMMdd)
     */
    public static String getTimeStamp1() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_TIME, Locale.getDefault());
        return sdf.format(new Date());
    }
    /**
     * 获取3个月之前的时间
     * 
     * @return 3个月之前的时间(yyyy.MM.dd)
     */
    public static String getDotTimeThreeMonthsAgo() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, -3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime(), DateFormatUtils.PATTERN_DOT_TIME);
    }

    /**
     * 获取3个月之前的时间
     * 
     * @return 3个月之前的时间(yyyyMMdd)
     */
    public static String getTimeThreeMonthsAgo() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, -3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime(), DateFormatUtils.PATTERN_TIME);
    }
    
    /**
     * 获取2年后时间
     * 
     * @return 3个月之前的时间(yyyyMMdd) +12
     */
    public static String getAfterAYearDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, 21);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime(), DateFormatUtils.PATTERN_TIME);
    }
    
    /**
     * 获取2年后时间
     * 
     * @return 3个月之前的时间(yyyyMMdd) +12
     */
    public static String getAfterTwoYearDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, 24);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime(), DateFormatUtils.PATTERN_TIME);
    }
    
    /**
     * 将一个日期字串按照指定模式格式化
     * 
     * @param date
     *            日期字串
     * @param fromPattern
     *            日期的原始格式
     * @param toPattern
     *            要显示的格式
     * @return 格式化后的字串
     */
    public static String format(String date, String fromPattern, String toPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern, Locale.getDefault());
        try {
            Date d = sdf.parse(date);
            return format(d, toPattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将一个日期字串按照指定模式格式化
     * 
     * @param date
     *            日期
     * @param pattern
     *            要显示的格式
     * @return 格式化后的字串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将一个日期格式的字符串转成Date对象
     * 
     * @param date
     *            日期格式的字串
     * @param pattern
     *            字串的格式
     * @return Date对象
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间和指定时间的间隔
     * 
     * @param context
     *            上下文
     * @param lastTime
     *            要比较的时间
     * @return 时间间隔
     */
    public static String getPassedTimes(Context context, long lastTime) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastTime;
        long timeIntoFormat;
        Resources res = context.getResources();
        String passedTime;
        if (timePassed < ONE_MINUTE) {
            passedTime = res.getString(R.string.time_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            passedTime = String.format(res.getString(R.string.time_minute), timeIntoFormat);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            passedTime = String.format(res.getString(R.string.time_hour), timeIntoFormat);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            passedTime = String.format(res.getString(R.string.time_day), timeIntoFormat);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            passedTime = String.format(res.getString(R.string.time_month), timeIntoFormat);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            passedTime = String.format(res.getString(R.string.time_year), timeIntoFormat);
        }
        return passedTime;
    }

    public static String getPassedTime(Context context, long lastTime) {

//        Date date = parse("2015-6-20 12","yyyy-MM-dd HH");
//        lastTime = date.getTime();
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastTime;
        Resources res = context.getResources();
        String passedTime;


        if (timePassed < ONE_MINUTE) {
            return passedTime = res.getString(R.string.time_just_now);
        }
        String dates = format(new Date(currentTime),PATTERN_BAR_TIME)+" 23:59:59";
        Date d = parse(dates,PATTERN_FULL);
        timePassed = d.getTime() - lastTime;
        if (timePassed < ONE_DAY) {
            passedTime = format(new Date(lastTime), PATTERN_H_S);
        }
        if(ONE_DAY < timePassed && timePassed < ONE_DAY*2){
            return passedTime = res.getString(R.string.yesterday);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(currentTime));
        int w = 7 - cal.get(Calendar.DAY_OF_WEEK);
        currentTime = d.getTime() + w * ONE_DAY;
        timePassed = currentTime - lastTime;
        if(timePassed < ONE_DAY * 7){
            return passedTime = getWeekOfDate(new Date(lastTime));
        } else {
            passedTime = format(new Date(lastTime),PATTERN_SLASH_TIME);
            String str=passedTime.substring(2,passedTime.length());
            passedTime=str;
        }
        return passedTime;
    }
    /***
     * 取当前时间的下一天
     * @return
     */
    public static Date getNextDay() {
    	Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}


    /**
     * 当前时间是否超过七天
     * @param context
     * @param saveTime
     * @return
     */
    public static boolean isUpdate(Context context,long saveTime){
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - saveTime;
        if(timePassed >= SEVEN_DAY){
            return true;
        }
        return false;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

}
