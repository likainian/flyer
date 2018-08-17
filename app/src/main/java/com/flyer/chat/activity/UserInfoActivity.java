package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2018/8/10.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mToolbarLeft;
    private TextView mToolbarMiddle;
    private LinearLayout mHead;
    private LinearLayout mName;
    private LinearLayout mAge;
    private LinearLayout mCode;
    private LinearLayout mLocation;
    private LinearLayout mSign;
    private TextView mBtnFinish;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    public static void startActivity(Context context, boolean register) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("register", register);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarLeft.setOnClickListener(this);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mHead = findViewById(R.id.head);
        mHead.setOnClickListener(this);
        mName = findViewById(R.id.name);
        mName.setOnClickListener(this);
        mAge = findViewById(R.id.age);
        mAge.setOnClickListener(this);
        mCode = findViewById(R.id.code);
        mCode.setOnClickListener(this);
        mLocation = findViewById(R.id.location);
        mLocation.setOnClickListener(this);
        mSign = findViewById(R.id.sign);
        mSign.setOnClickListener(this);
        mBtnFinish = findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(this);
        mToolbarMiddle.setText("个人信息");
        if(getIntent().getBooleanExtra("register",false)){
            mBtnFinish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.head:
//                new SelectDialog(this,)
                break;
        }
    }
}
