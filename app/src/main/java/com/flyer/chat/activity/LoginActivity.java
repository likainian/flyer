package com.flyer.chat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/7/9.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mUserPhoto;
    private EditText mUserName;
    private ImageView mUserMore;
    private EditText mPassword;
    private ImageView mSeePassword;
    private CheckBox mSavePassword;
    private TextView mForgetPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private TextView mLoginHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initData() {
        JMessageClient.login(SharedPreferencesHelper.getInstance().getUserName(), SharedPreferencesHelper.getInstance().getPassWord(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtil.i("ttt", "login:" + i + "==" + s);
                if (BuildConfig.DEBUG) ToastHelper.showToast(i == 0 ? "im登陆成功" : "im登陆失败");
                if (i == 0) {
                    MainActivity.startActivity(LoginActivity.this);
                }
            }
        });
    }

    private void initView() {
        mUserPhoto = findViewById(R.id.user_photo);
        mUserName = findViewById(R.id.edit_user_name);
        mUserMore = findViewById(R.id.user_more);
        mPassword = findViewById(R.id.password);
        mSeePassword = findViewById(R.id.see_password);
        mSavePassword = findViewById(R.id.save_password);
        mForgetPassword = findViewById(R.id.forget_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
        mLoginHelp = findViewById(R.id.login_help);

        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                break;
            case R.id.btn_register:
                RegisterActivity.startActivity(this);
                break;
        }
    }
}
