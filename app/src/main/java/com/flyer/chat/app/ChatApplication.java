package com.flyer.chat.app;

import android.app.Application;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.receiver.EventReceiver;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;

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
    public static void updateUser(){
        RetrofitService.getInstance().requestPost(ConstantUtil.ACCOUNT_ADD_USER, SharedPreferencesHelper.getInstance().getUser(),new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) ToastHelper.showToast("更新成功");
            }
        });
    }
}
