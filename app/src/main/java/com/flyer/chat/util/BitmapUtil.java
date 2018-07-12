package com.flyer.chat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mike.li on 2018/7/9.
 */

public class BitmapUtil {
    public static File getCompressImg(String imgPath){
        Bitmap bitmap = getBitmapByPath(imgPath);
        compressImage(bitmap,imgPath);
        return new File(imgPath);
    }

    public static Bitmap getBitmapByPath(String imgPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(imgPath, newOpts);// 此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > 720) {// 如果宽度大的话根据宽度固定大小缩放
            be = Math.round(newOpts.outWidth / 720);
        } else if (w < h && h > 1280) {// 如果高度高的话根据宽度固定大小缩放
            be = Math.round(newOpts.outHeight / 1280);
        }
        if (be <= 0)be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(imgPath, newOpts);
        } catch (Exception e) {
            return null;
        }
    }


    public static void compressImage(Bitmap image,String file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;// 每次都减少10
            if (options < 0) {
                break;
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
