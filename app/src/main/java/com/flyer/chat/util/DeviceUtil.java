package com.flyer.chat.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;

import com.flyer.chat.app.ChatApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by mike.li on 2018/5/16.
 */

public class DeviceUtil {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    //屏幕的高
    public static int getDisplayHeight(Context context) {
        return Math.max(context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);
    }
    //屏幕的宽
    public static int getDisplayWidth(Context context) {
        return Math.min(context.getResources().getDisplayMetrics().widthPixels, context
                        .getResources().getDisplayMetrics().heightPixels);
    }

    public static String getSavePicturePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"flyer"+File.separator+"picture"+File.separator);
    }
    public static String getSaveFilePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"flyer"+File.separator+"file"+File.separator);
    }

    public static UUID getDeviceUuid() {
        UUID uuid = null;
        final String androidId = Settings.System.getString(ChatApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uuid==null){
            uuid= UUID.randomUUID();
        }
        return uuid;
    }
}
