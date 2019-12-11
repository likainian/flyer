package com.flyer.chat.activity.account;

import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.activity.HomeActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.SharedPreferencesUtil;

/**
 * Created by mike.li on 2018/8/13.
 */

public class LauncherActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String mobileNo = SharedPreferencesUtil.getInstance().getMobileNo();
        String passWord = SharedPreferencesUtil.getInstance().getPassWord();
        if(CheckUtil.isNotEmpty(mobileNo)&&CheckUtil.isNotEmpty(passWord)){
            HomeActivity.startActivity(this);
        }else {
            LoginActivity.startActivity(this);
        }
    }
}
