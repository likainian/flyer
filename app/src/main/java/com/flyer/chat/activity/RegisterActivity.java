package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastHelper;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/8/8.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mToolbarLeft;
    private TextView mToolbarMiddle;
    private TextView mToolbarRight;
    private RelativeLayout mToolbar;
    private ImageView mUserPhoto;
    private EditText mUserName;
    private EditText mPassword;
    private ImageView mSeePassword;
    private CheckBox mSavePassword;
    private Button mBtnRegister;
    private TextView mLoginHelp;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mToolbar = findViewById(R.id.toolbar);
        mUserPhoto = findViewById(R.id.user_photo);
        mUserName = findViewById(R.id.edit_user_name);
        mPassword = findViewById(R.id.password);
        mSeePassword = findViewById(R.id.see_password);
        mSavePassword = findViewById(R.id.save_password);
        mBtnRegister = findViewById(R.id.btn_register);
        mLoginHelp = findViewById(R.id.login_help);

        mBtnRegister.setOnClickListener(this);
        mUserName.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnRegister.setEnabled(registerEnable());
            }
        });
        mPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnRegister.setEnabled(registerEnable());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                JMessageClient.register(mUserName.getText().toString().trim(), mPassword.getText().toString().trim(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if(i==0){
                            EditUserInfoActivity.startActivity(RegisterActivity.this);
                        }else if(898001==i){
                            ToastHelper.showToast("用户已存在，请直接登陆!");
                        }else {
                            ToastHelper.showToast(s);
                        }
                        LogUtil.i("ttt","register:"+i+"=="+s);
                    }
                });
                break;
        }
    }

    private boolean registerEnable(){
        return CheckUtil.isNotEmpty(mUserName.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mPassword.getText().toString().trim());
    }

}
