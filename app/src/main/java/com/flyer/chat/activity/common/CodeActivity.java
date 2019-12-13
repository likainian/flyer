package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by Administrator on 2018/8/25.
 */

public class CodeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView codeView;

    public static void startActivity(Context context){
        context.startActivity(new Intent(context, CodeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        FrameLayout toolbarLeft = findViewById(R.id.toolbar_left);
        toolbarLeft.setOnClickListener(this);
        codeView = findViewById(R.id.code_view);
        setHead();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                onBackPressed();
                break;
        }
    }

    private void setHead(){
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
        canvas.save();
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
