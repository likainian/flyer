package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.network.UMSCallback;
import com.flyer.chat.util.CodeUtil;
import com.flyer.chat.util.GlideOptions;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

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
        UMSSDK.getLoginUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(final User user) {
                super.onSuccess(user);
                String[] strings = user.avatar.get();
                if(strings==null){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
                    Bitmap code = CodeUtil.encode(user.phone.get());
                    Bitmap logoCode = addLogo(code, bitmap);
                    codeView.setImageBitmap(logoCode);
                }else {
                    Glide.with(CodeActivity.this).applyDefaultRequestOptions(GlideOptions.UserOptions()).asBitmap().load(strings[0])
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    Bitmap code = CodeUtil.encode(user.phone.get());
                                    Bitmap logoCode = addLogo(code, resource);
                                    codeView.setImageBitmap(logoCode);
                                }
                            });
                }
            }
        });
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
