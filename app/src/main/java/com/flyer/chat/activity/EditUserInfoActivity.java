package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;

import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2018/8/10.
 */

public class EditUserInfoActivity extends BaseActivity{
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,EditUserInfoActivity.class));
    }
}
