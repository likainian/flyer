package com.flyer.chat.app;

import android.app.Application;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;

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
        updateUser();
    }
    public static void updateUser(){
        RetrofitService.getInstance().requestPost(ConstantUtil.ACCOUNT_ADD_USER, SharedPreferencesHelper.getInstance().getUser(),new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) ToastHelper.showToast("登陆成功");
            }
        });
    }
}
