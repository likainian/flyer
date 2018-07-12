package com.flyer.chat.util;

import android.util.Log;

import com.flyer.chat.BuildConfig;


/**
 * Created by mike.li on 2018/4/20.
 */

public class LogUtil {
    public static void i(String message){
        if(BuildConfig.DEBUG){
            Log.i(generateTag(), message);
        }
    }
    public static void i(String tag, String message){
        if(BuildConfig.DEBUG){
            Log.i(tag, message);
        }
    }
    private static String generateTag() {
        StackTraceElement sElements = new Throwable().getStackTrace()[2];
        String className = sElements.getFileName();
        String methodName = sElements.getMethodName();
        int lineNumber = sElements.getLineNumber();
        return String.format("%s:%s(%s:%d)", className, methodName,className, lineNumber);
    }
}
