package com.flyer.pushsdk;

import android.util.Log;

/**
 * Created by mike.li on 2018/8/2.
 */

public class Logger {
    private static boolean logcat = true;
    public static void i(String msg){
        if(logcat)Log.i("ttt",msg);
    }
}
