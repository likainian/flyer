package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.LogUtil;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by mike.li on 2020/7/9.
 */
public class TestLiveActivity extends ToolbarActivity {
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestLiveActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_live);
        TXLivePushConfig mLivePushConfig  = new TXLivePushConfig();
        TXLivePusher mLivePusher = new TXLivePusher(this);

        // 一般情况下不需要修改 config 的默认配置
        mLivePusher.setConfig(mLivePushConfig);
        //启动本地摄像头预览
        TXCloudVideoView mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
        mLivePusher.startCameraPreview(mPusherView);
        String rtmpURL = "rtmp://test.com/live/xxxxxx"; //此处填写您的 rtmp 推流地址
        int ret = mLivePusher.startPusher(rtmpURL.trim());
        if (ret == -5) {
            LogUtil.i("ttt", "startRTMPPush: license 校验失败");
        }

        //mPlayerView 即 step1 中添加的界面 view
        TXCloudVideoView mView = (TXCloudVideoView) findViewById(R.id.video_view);

        //创建 player 对象
        TXLivePlayer mLivePlayer = new TXLivePlayer(this);

        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(mView);
        String flvUrl = "http://2157.liveplay.myqcloud.com/live/2157_xxxx.flv";
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐 FLV
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
