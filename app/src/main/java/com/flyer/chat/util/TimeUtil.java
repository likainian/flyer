package com.flyer.chat.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mike.li on 2018/7/9.
 */

public class TimeUtil {
    public final static String FORMAT_DAY = "yyyy-MM-dd";
    public final static String FORMAT_YEAR_MONTH = "yyyy-MM";
    public final static String YMDHM = "yyyy-MM-dd HH:mm";
    public final static String YMD = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_SECOND = "HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(YMD,Locale.CHINA);
    public static void switchLocal(Locale locale) {
        sdf = new SimpleDateFormat(YMD,locale);
    }

    public static String longToYMD(Long time){
        sdf.applyPattern(YMD);
        return sdf.format(time);
    }
    public static String longToYMDHM(Long time){
        sdf.applyPattern(YMDHM);
        return sdf.format(time);
    }
}
