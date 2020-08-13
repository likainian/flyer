package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2020/8/13.
 */
public class PhotoPlayActivity extends BaseActivity {
    public static void startActivity(Context context, String path){
        Intent intent = new Intent(context, PhotoPlayActivity.class);
        intent.putExtra("path",path);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_play);
        ImageView mIvPicture = findViewById(R.id.iv_picture);
        ImageView mIvBack = findViewById(R.id.iv_back);
        String path = getIntent().getStringExtra("path");
        Glide.with(this).load(path).into(mIvPicture);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
