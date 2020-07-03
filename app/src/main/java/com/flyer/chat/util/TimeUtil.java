package com.flyer.chat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mike.li on 2018/7/9.
 */

public class TimeUtil {
    public final static String FORMAT_YEAR_MONTH = "yyyy-MM";
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String FORMAT_YEAR_MONTH_DAY_APM = "yyyy-MM-dd a";
    public final static String FORMAT_YEAR_MONTH_DAY_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_YEAR_MONTH_DAY_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_YEAR_MONTH_DAY_WEEK = "yyyy-MM-dd EEEE";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_TIME_SECOND = "HH:mm:ss";

    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;// 小时
    public static final long DAY = 24 * HOUR;// 天
    public static final long MONTH = 30 * DAY;// 月
    public static final long YEAR = 365 * DAY;// 年
    private static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY,Locale.CHINA);
    public static void switchLocal(Locale locale) {
        sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY,locale);
    }

    public static Long parseToLong(String time,String format){
        sdf.applyPattern(format);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static String longToString(long time,String format){
        sdf.applyPattern(format);
        return sdf.format(time);
    }

    public static int longToAge(Long time){
        Calendar calNow = Calendar.getInstance();
        calNow.setTimeInMillis(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        if (calNow.before(cal)) {
            return 0;
        }
        int yearNow = calNow.get(Calendar.YEAR);
        int monthNow = calNow.get(Calendar.MONTH);
        int dayOfMonthNow = calNow.get(Calendar.DAY_OF_MONTH);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

    /**
     * @param time
     * @return FORMAT_YEAR_MONTH_DAY_WEEK = "yyyy-MM-dd EEEE"
     */
    public static String longToYearMonthDayWeek(long time) {
        return longToString(time,FORMAT_YEAR_MONTH_DAY_WEEK);
    }
    public static String longToYearMonthDayTimeSecond(long time) {
        return longToString(time,FORMAT_YEAR_MONTH_DAY_TIME_SECOND);
    }
}
