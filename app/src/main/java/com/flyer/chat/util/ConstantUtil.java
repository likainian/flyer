package com.flyer.chat.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ConstantUtil {

    public static String getBaseUrl() {
        return "";
    }
    public static String getCameraPhonePath(String fileName){
        return FileUtil.createPathAndFile(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+File.separator+"picture"+File.separator+fileName+".jpg");
    }
    public static String getSavePicturePath(String fileName){
        return FileUtil.createPathAndFile(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+File.separator+"cache"+File.separator+fileName+".jpg");
    }
}
