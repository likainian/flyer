package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2018/11/6.
 */

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        FrameLayout mToolbarLeft = findViewById(R.id.toolbar_left);
        LinearLayout mFeedSimpleBack = findViewById(R.id.feed_simple_back);
        LinearLayout mFeedUiBack = findViewById(R.id.feed_ui_back);
        LinearLayout mFeedFunBack = findViewById(R.id.feed_fun_back);
        LinearLayout mFeedNewBack = findViewById(R.id.feed_new_back);
        LinearLayout mFeedBugBack = findViewById(R.id.feed_bug_back);

        mToolbarLeft.setOnClickListener(this);
        mFeedSimpleBack.setOnClickListener(this);
        mFeedUiBack.setOnClickListener(this);
        mFeedFunBack.setOnClickListener(this);
        mFeedNewBack.setOnClickListener(this);
        mFeedBugBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.feed_simple_back:
                FeedSimpleBackActivity.startActivity(this);
                break;
            case R.id.feed_ui_back:
                FeedUiBackActivity.startActivity(this);
                break;
            case R.id.feed_fun_back:
                FeedFunBackActivity.startActivity(this);
                break;
            case R.id.feed_new_back:
                FeedNewBackActivity.startActivity(this);
                break;
            case R.id.feed_bug_back:
                FeedBugBackActivity.startActivity(this);
                break;
        }
    }
}
