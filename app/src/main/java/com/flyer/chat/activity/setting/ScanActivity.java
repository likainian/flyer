package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.ToastHelper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ScanActivity extends BaseActivity{
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,ScanActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    //识别二维码的函数
    public void recogQRcode(Bitmap QRbmp){
        int width = QRbmp.getWidth();
        int height = QRbmp.getHeight();
        int[] data = new int[width * height];
        QRbmp.getPixels(data, 0, width, 0, 0, width, height);    //得到像素
        RGBLuminanceSource source = new RGBLuminanceSource(width,height,data);   //RGBLuminanceSource对象
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result re = null;
        try {
            //得到结果
            re = reader.decode(bitmap1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        //Toast出内容
        if (re != null) {
            ToastHelper.showToast(re.getText());
        }
    }
}
