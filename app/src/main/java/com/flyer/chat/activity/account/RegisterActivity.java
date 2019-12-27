package com.flyer.chat.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.feedback.QuestionActivity;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.KeyBoardUtil;
import com.flyer.chat.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2018/8/8.
 */

public class RegisterActivity extends ToolbarActivity implements View.OnClickListener{
    private ScrollView mScrollView;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private ImageView mIvPassword;
    private TextView mTvRegister;
    private EditText mEtCode;
    private TextView mTvCode;
    private Disposable disposable;

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
        mScrollView = findViewById(R.id.scroll_view);
        mEtPhone = findViewById(R.id.et_phone);
        mEtPassword = findViewById(R.id.et_password);
        mIvPassword = findViewById(R.id.iv_password);
        mTvRegister = findViewById(R.id.tv_register);
        mEtCode = findViewById(R.id.et_code);
        mTvCode = findViewById(R.id.tv_code);
        TextView mTvHelp = findViewById(R.id.tv_help);

        setToolbarMiddleText("注册账号");
        mTvRegister.setOnClickListener(this);
        mTvHelp.setOnClickListener(this);
        mIvPassword.setOnClickListener(this);
        mTvCode.setOnClickListener(this);
        mEtPhone.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvRegister.setEnabled(registerEnable());
            }
        });
        mEtPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvRegister.setEnabled(registerEnable());
            }
        });
        mEtCode.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvRegister.setEnabled(registerEnable());
            }
        });
        KeyBoardUtil.register(this, new KeyBoardUtil.KeyBoardStatusListener() {
            @Override
            public void onKeyBoardStateChanged(boolean isShow, int height) {
                int keyBoardTop = DeviceUtil.getDisplayHeight(RegisterActivity.this) - height;
                int[] location = new int[2];
                mTvRegister.getLocationOnScreen(location);
                final int scrollY = location[1] + mTvRegister.getHeight()-keyBoardTop;
                if (scrollY > 0) {
                    mScrollView.smoothScrollBy(0, scrollY);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_password:
                switchShowPassword();
                break;
            case R.id.tv_register:
                checkCode();
                break;
            case R.id.tv_help:
                QuestionActivity.startActivity(this,QuestionActivity.QUESTION_TYPE);
                break;
            case R.id.tv_code:
                getCode();
                break;
        }
    }

    private boolean registerEnable(){
        return CheckUtil.isNotEmpty(mEtPhone.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mEtCode.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mEtPassword.getText().toString().trim());
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

    private void checkCode(){
        BmobUser.signOrLoginByMobilePhone(mEtPhone.getText().toString().trim(), mEtCode.getText().toString().trim(), new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e!=null){
                    ToastUtil.showToast("该账号已经注册，请登录");
                    finish();
                }else {
                    bmobUser.setPassword(mEtPassword.getText().toString().trim());
                    bmobUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            ToastUtil.showToast("注册成功");
                            finish();
                        }
                    });
                }
            }
        });
    }
}