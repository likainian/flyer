package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2020/8/13.
 */
public class VideoPlayActivity extends BaseActivity {
    public static void startActivity(Context context,String path){
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("path",path);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        VideoView mVideoView = findViewById(R.id.video_view);
        ImageView mIvBack = findViewById(R.id.iv_back);
        String path = getIntent().getStringExtra("path");
        mVideoView.setVideoPath(path);
        mVideoView.start();
        /**
         * 视频播放完成时回调
         */
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
