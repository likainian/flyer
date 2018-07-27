package com.flyer.chat.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ConstantUtil {
    public static final String ACCOUNT_ADD_USER = "addUser";
    public static final String ACCOUNT_DELETE_USER = "deleteUser";
    public static String getBaseUrl() {
        return "http://192.168.2.64:8081/";
    }
    public static String getCameraPhonePath(String fileName){
        return FileUtil.createPathAndFile(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+File.separator+"picture"+File.separator+fileName+".jpg");
    }
    public static String getSavePicturePath(String fileName){
        return FileUtil.createPathAndFile(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+File.separator+"cache"+File.separator+fileName+".jpg");
    }

    public static String getImageUrl(String imageId){
        return getBaseUrl()+"image/"+imageId+".jpg";
    }

}
