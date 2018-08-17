package com.flyer.chat.activity;

import android.os.Bundle;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/8/13.
 */

public class LauncherActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login(SharedPreferencesHelper.getInstance().getUserName(), SharedPreferencesHelper.getInstance().getPassWord());
    }

    private void login(final String name, final String password) {
        LogUtil.i("ttt",name+"="+password);
        JMessageClient.login(name,password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtil.i("ttt", "login:" + i + "==" + s);
                if (BuildConfig.DEBUG) ToastHelper.showToast(i == 0 ? "im登陆成功" : "im登陆失败");
                if (i == 0) {
                    MainActivity.startActivity(LauncherActivity.this);
                    ChatApplication.updateUser();
                    finish();
                }else {
                    LoginActivity.startActivity(LauncherActivity.this);
                }
            }
        });
    }
}