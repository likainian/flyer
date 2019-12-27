package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.flyer.chat.R;
import com.flyer.chat.activity.feedback.QuestionActivity;
import com.flyer.chat.base.ToolbarActivity;

/**
 * Created by mike.li on 2018/11/6.
 */

public class FeedBackActivity extends ToolbarActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        setToolbarMiddleText("用户反馈");
        initView();
    }

    private void initView() {
        LinearLayout mLlSimple = findViewById(R.id.ll_simple);
        LinearLayout mLlUi= findViewById(R.id.ll_ui);
        LinearLayout mLlFun = findViewById(R.id.ll_fun);
        LinearLayout mLlNew = findViewById(R.id.ll_new);
        LinearLayout mLlBug = findViewById(R.id.ll_bug);

        mLlSimple.setOnClickListener(this);
        mLlUi.setOnClickListener(this);
        mLlFun.setOnClickListener(this);
        mLlNew.setOnClickListener(this);
        mLlBug.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_simple:
                QuestionActivity.startActivity(this,QuestionActivity.NORMAL_TYPE);
                break;
            case R.id.ll_ui:
                QuestionActivity.startActivity(this,QuestionActivity.UI_TYPE);
                break;
            case R.id.ll_fun:
                QuestionActivity.startActivity(this,QuestionActivity.FUN_TYPE);
                break;
            case R.id.ll_new:
                QuestionActivity.startActivity(this,QuestionActivity.NEW_TYPE);
                break;
            case R.id.ll_bug:
                QuestionActivity.startActivity(this,QuestionActivity.BUG_TYPE);
                break;
        }
    }
}
