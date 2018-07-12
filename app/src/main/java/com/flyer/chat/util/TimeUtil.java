package com.flyer.chat.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mike.li on 2018/7/9.
 */

public class TimeUtil {
    private static Locale locale;
    public static void switchLocal(Locale nlocale) {
        locale = nlocale;
    }
}
