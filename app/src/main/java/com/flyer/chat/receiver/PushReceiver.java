package com.flyer.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.flyer.chat.util.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mike.li on 2018/8/2.
 */

public class PushReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtil.i("onReceive - " + intent.getAction());
        if (bundle != null) {
            String title = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            LogUtil.i("自定义消息:"+title);
        }
    }
}
