package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2018/11/6.
 */

public class FeedNewBackActivity extends BaseActivity{
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedNewBackActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_new_back);
    }
}
