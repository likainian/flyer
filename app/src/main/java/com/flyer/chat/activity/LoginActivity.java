package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.dialog.TitleSelectDialog;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.KeyBoardUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/7/9.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }
    private ScrollView mLoginScroll;
    private ImageView mUserPhoto;
    private EditText mUserName;
    private ImageView mUserMore;
    private EditText mPassword;
    private ImageView mSeePassword;
    private CheckBox mSavePassword;
    private TextView mForgetPassword;
    private TextView mBtnLogin;
    private TextView mBtnRegister;
    private TextView mLoginHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mLoginScroll = findViewById(R.id.login_scroll);
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
        mUserName.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnLogin.setEnabled(loginEnable());
            }
        });
        mPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnLogin.setEnabled(loginEnable());
            }
        });
        KeyBoardUtil.register(this, new KeyBoardUtil.KeyBoardStatusListener() {
            @Override
            public void onKeyBoardStateChanged(boolean isShowKeyBoard, int keyBoardTop) {
                int[] location = new int[2];
                mBtnLogin.getLocationOnScreen(location);
                final int scrollY = location[1] + mBtnLogin.getHeight()-keyBoardTop;
                if (scrollY > 0) {
                    mLoginScroll.smoothScrollBy(0, scrollY);
                }
            }
        });
        if(CheckUtil.isEmpty(SharedPreferencesHelper.getInstance().getUserNameList())){
            mUserMore.setVisibility(View.GONE);
        }else {
            mUserMore.setVisibility(View.VISIBLE);
            mUserMore.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login(mUserName.getText().toString().trim(),mPassword.getText().toString().trim());
                break;
            case R.id.btn_register:
                RegisterActivity.startActivity(this);
                break;
            case R.id.user_more:
                List<String> userNameList = SharedPreferencesHelper.getInstance().getUserNameList();
                new TitleSelectDialog(this, userNameList, new TitleSelectDialog.OnSelectListener() {
                    @Override
                    public void OnSelect(int position) {
                        ToastHelper.showToast(String.valueOf(position));
                    }
                }).show();
                break;
        }
    }
    private boolean loginEnable(){
        return CheckUtil.isNotEmpty(mUserName.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mPassword.getText().toString().trim());
    }

    private void login(final String name, final String password) {
        LogUtil.i("ttt",name+"="+password);
        showLoadingDialog();
        JMessageClient.login(name,password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtil.i("ttt", "login:" + i + "==" + s);
                closeLoadingDialog();
                if (BuildConfig.DEBUG) ToastHelper.showToast(i == 0 ? "im登陆成功" : "im登陆失败");
                if (i == 0) {
                    SharedPreferencesHelper.getInstance().setUserName(name);
                    SharedPreferencesHelper.getInstance().setPassWord(password);
                    MainActivity.startActivity(LoginActivity.this);
                    ChatApplication.updateUser();
                    finish();
                }
            }
        });
    }
}
