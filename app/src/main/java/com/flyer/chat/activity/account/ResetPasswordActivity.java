package com.flyer.chat.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.ClearEditText;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2019/10/29.
 */
public class ResetPasswordActivity extends ToolbarActivity implements View.OnClickListener {
    private ClearEditText mEtPhone;
    private ClearEditText mEtCode;
    private ClearEditText mEtPassword;
    private TextView mTvCode;
    private TextView mBtNext;
    private Disposable disposable;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ResetPasswordActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }


    private void initView() {
        mEtPhone = findViewById(R.id.et_phone);
        mEtCode = findViewById(R.id.et_code);
        mEtPassword = findViewById(R.id.et_password);
        mTvCode = findViewById(R.id.tv_code);
        mBtNext = findViewById(R.id.bt_next);

        setToolbarMiddleText("重置密码");
        mBtNext.setOnClickListener(this);
        mTvCode.setOnClickListener(this);
        mBtNext.setEnabled(nextEnable());
        mEtPhone.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtNext.setEnabled(nextEnable());
            }
        });
        mEtCode.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtNext.setEnabled(nextEnable());
            }
        });
        mEtPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtNext.setEnabled(nextEnable());
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_code:
                getCode();
                break;
            case R.id.bt_next:
                resetPassword();
                break;
        }
    }
    private boolean nextEnable(){
        return CheckUtil.isNotEmpty(mEtPhone.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mEtCode.getText().toString().trim());
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

    private void resetPassword(){
        BmobUser.resetPasswordBySMSCode(mEtCode.getText().toString().trim(), mEtPassword.getText().toString().trim(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtil.showToast("重置成功");
                    LoginActivity.startActivity(ResetPasswordActivity.this);
                } else {
                    ToastUtil.showToast(e.getMessage());
                }
            }
        });
    }
}
