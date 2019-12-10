package com.flyer.chat.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.feedback.QuestionActivity;
import com.flyer.chat.activity.home.HomeActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.dialog.TitleSelectDialog;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.KeyBoardUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2018/7/9.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private Disposable disposable;

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }
    private ScrollView mScrollView;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtCode;
    private ImageView mIvPassword;
    private TextView mTvCode;
    private TextView mTvLogin;
    private LinearLayout mLlPassword;
    private LinearLayout mLlCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mScrollView = findViewById(R.id.scroll_view);
        mEtPhone = findViewById(R.id.et_phone);
        ImageView mIvMore = findViewById(R.id.iv_more);
        mEtPassword = findViewById(R.id.et_password);
        mIvPassword = findViewById(R.id.iv_password);
        mEtCode = findViewById(R.id.et_code);
        mTvCode = findViewById(R.id.tv_code);
        TextView mTvForgetPassword = findViewById(R.id.tv_forget_password);
        TextView mTvSwitch = findViewById(R.id.tv_switch);
        mTvLogin = findViewById(R.id.tv_login);
        TextView mTvRegister = findViewById(R.id.tv_register);
        TextView mTvHelp = findViewById(R.id.tv_help);
        mLlPassword = findViewById(R.id.ll_password);
        mLlCode = findViewById(R.id.ll_code);

        mLlCode.setVisibility(View.GONE);
        mTvLogin.setOnClickListener(this);
        mTvHelp.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mTvSwitch.setOnClickListener(this);
        mIvPassword.setOnClickListener(this);
        mTvCode.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mEtPhone.setText(SharedPreferencesHelper.getInstance().getPhoneNum());
        mEtPassword.setText(SharedPreferencesHelper.getInstance().getPassword());

        mEtPhone.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvLogin.setEnabled(loginEnable());
            }
        });
        mEtPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvLogin.setEnabled(loginEnable());
            }
        });
        KeyBoardUtil.register(this, new KeyBoardUtil.KeyBoardStatusListener() {
            @Override
            public void onKeyBoardStateChanged(boolean isShow, int height) {
                int keyBoardTop = DeviceUtil.getDisplayHeight(LoginActivity.this) - height;
                int[] location = new int[2];
                mTvLogin.getLocationOnScreen(location);
                final int scrollY = location[1] + mTvLogin.getHeight()-keyBoardTop;
                if (scrollY > 0) {
                    mScrollView.smoothScrollBy(0, scrollY);
                }
            }
        });
        if(CheckUtil.isEmpty(SharedPreferencesHelper.getInstance().getUserNameList())){
            mIvMore.setVisibility(View.GONE);
        }else {
            mIvMore.setVisibility(View.VISIBLE);
            mIvMore.setOnClickListener(this);
        }

        mTvLogin.setEnabled(loginEnable());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forget_password:
                ResetPasswordActivity.startActivity(this);
                break;
            case R.id.tv_register:
                RegisterActivity.startActivity(this);
                break;
            case R.id.iv_password:
                switchShowPassword();
                break;
            case R.id.iv_more:
                final List<String> userNameList = SharedPreferencesHelper.getInstance().getUserNameList();
                new TitleSelectDialog(this)
                        .setTitle("选择手机号")
                        .setList(userNameList)
                        .setCheck(mEtPhone.getText().toString().trim())
                        .setOnSelectListener(new TitleSelectDialog.OnSelectListener() {
                            @Override
                            public void OnSelect(int position) {
                                mEtPhone.setText(userNameList.get(position));
                            }
                        }).show();
                break;
            case R.id.tv_help:
                QuestionActivity.startActivity(this);
                break;
            case R.id.tv_switch:
                switchLogin();
                break;
            case R.id.tv_code:
                getCode();
                break;
        }
    }

    private void switchLogin(){
        if(mLlCode.isShown()){
            mLlPassword.setVisibility(View.VISIBLE);
            mLlCode.setVisibility(View.GONE);
        }else {
            mLlPassword.setVisibility(View.GONE);
            mLlCode.setVisibility(View.VISIBLE);
        }
    }

    private void switchShowPassword() {
        if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mIvPassword.setImageResource(R.drawable.password_open_eye);
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mIvPassword.setImageResource(R.drawable.password_colse_eye);
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private boolean loginEnable(){
        return CheckUtil.isNotEmpty(mEtPhone.getText().toString().trim())
                &&((mLlCode.isShown()&&CheckUtil.isNotEmpty(mEtCode.getText().toString().trim()))
                ||(mLlPassword.isShown()&&CheckUtil.isNotEmpty(mEtPassword.getText().toString().trim())));
    }

    private void login() {
        if(mLlCode.isShown()){
            BmobUser.loginBySMSCode(mEtPhone.getText().toString().trim(), mEtCode.getText().toString().trim(), new LogInListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null) {
                        HomeActivity.startActivity(LoginActivity.this);
                    } else {
                        ToastUtil.showToast(e.getMessage());
                    }
                }
            });
        }else {
            BmobUser.loginByAccount(mEtPhone.getText().toString().trim(),mEtPassword.getText().toString().trim(), new LogInListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        SharedPreferencesHelper.getInstance().setPhoneNum(mEtPhone.getText().toString().trim());
                        SharedPreferencesHelper.getInstance().setPassword(mEtPassword.getText().toString().trim());
                        HomeActivity.startActivity(LoginActivity.this);
                    } else {
                        ToastUtil.showToast(e.getMessage());
                    }
                }
            });
        }
    }

    public void showVerifyCode() {
        mTvCode.setEnabled(false);
        disposable = Observable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        if(aLong<60){
                            mTvCode.setText(String.format(CommonUtil.getString(R.string.second), 60 - aLong));
                        }else {
                            mTvCode.setText("点击获取");
                            mTvCode.setEnabled(true);
                            disposable.dispose();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable.getMessage());
                        mTvCode.setText("点击获取");
                        mTvCode.setEnabled(true);
                    }
                });
    }

    private void getCode(){
        BmobSMS.requestSMSCode(mEtPhone.getText().toString().trim(), "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    showVerifyCode();
                }
            }
        });
    }
}
