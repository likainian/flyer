package com.flyer.chat.activity;

import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.activity.account.LoginActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.activity.home.HomeActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by mike.li on 2018/8/13.
 */

public class LauncherActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (BmobUser.isLogin()) {
            HomeActivity.startActivity(this);
        } else {
            LoginActivity.startActivity(this);
        }
    }
}
