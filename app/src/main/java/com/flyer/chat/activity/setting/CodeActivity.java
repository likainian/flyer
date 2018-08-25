package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.LogUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2018/8/25.
 */

public class CodeActivity extends BaseActivity {
    private ImageView codeView;

    public static void startActivity(Context context){
        context.startActivity(new Intent(context, CodeActivity.class));
    }
    private UserInfo myInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        codeView = findViewById(R.id.code_view);
        myInfo = JMessageClient.getMyInfo();
        setHead();
    }
    private void setHead(){
        myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                LogUtil.i(i+s);
                if(i==0){
                    Bitmap code = generateBitmap(myInfo.getUserName());
                    Bitmap logoCode = addLogo(code, bitmap);
                    codeView.setImageBitmap(logoCode);
                }
            }
        });
    }

    //对输入的内容生成二维码
    private Bitmap generateBitmap(String content) {
        int width = 500;int height = 500;
        if("".equals(content)||null==content){
            Toast.makeText(CodeActivity.this,"输入内容为空!",Toast.LENGTH_SHORT).show();
            return null;
        }
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    //添加二维码中心logo
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        if (null==logoBitmap){
            Toast.makeText(CodeActivity.this,"所选图片为空!",Toast.LENGTH_SHORT).show();
            return null;
        }
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

}
