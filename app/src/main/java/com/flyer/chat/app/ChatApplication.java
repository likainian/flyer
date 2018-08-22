package com.flyer.chat.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.receiver.EventReceiver;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ChatApplication extends Application {
    private static ChatApplication instance;
    public static synchronized ChatApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        //极光统计
        JAnalyticsInterface.setDebugMode(BuildConfig.DEBUG);
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.initCrashHandler(this);
        //极光im
        JMessageClient.setDebugMode(BuildConfig.DEBUG);
        JMessageClient.init(this);
        JMessageClient.registerEventReceiver(new EventReceiver());
    }
}
