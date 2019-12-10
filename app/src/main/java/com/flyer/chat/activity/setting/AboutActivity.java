package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;

public class AboutActivity extends ToolbarActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        setToolbarMiddleText("关于微聊");
    }

    private void initView() {
        TextView mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText("V"+ BuildConfig.VERSION_NAME);
    }
}
