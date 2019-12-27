package com.flyer.chat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mike.li on 2018/5/28.
 */

public class FileUtil {
    public static void saveCacheBitmap(String name, Bitmap bitmap) {
        String fullPath = getSavePicturePath()+name+".jpg";
        File file = new File(fullPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getCacheBitmap(String name) {
        String fullPath = getSavePicturePath()+name+".jpg";
        return BitmapFactory.decodeFile(fullPath);
    }

    public static String createPathAndFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) return filePath;
        String parent = file.getParent();
        String path = createPath(parent);
        if (path != null) {
            try {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    return filePath;
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String createPath(String path) {
        File file = new File(path);
        if (file.exists()) return path;
        boolean mkdirs = file.mkdirs();
        if (mkdirs) {
            return path;
        } else {
            return null;
        }
    }

    public static String getSavePicturePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"flyer"+File.separator+"picture"+File.separator);
    }
    public static String getSaveFilePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"flyer"+File.separator+"file"+File.separator);
    }

}
