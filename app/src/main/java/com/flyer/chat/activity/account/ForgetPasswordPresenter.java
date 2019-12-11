package com.flyer.chat.activity.account;

import com.flyer.chat.network.UMSCallback;
import com.mob.ums.UMSSDK;

/**
 * Created by mike.li on 2018/11/5.
 */

public class ForgetPasswordPresenter implements ForgetPasswordContract.ForgetPasswordPresenter {
    private ForgetPasswordContract.ForgetPasswordView mView;

    public ForgetPasswordPresenter(ForgetPasswordContract.ForgetPasswordView mView) {
        this.mView = mView;
    }

    @Override
    public void sendVerifyCode(String country, String mobileNo) {
        UMSSDK.sendVerifyCodeForResetPassword("86",mobileNo,new UMSCallback<Boolean>(){
            @Override
            public void onSuccess(Boolean o) {
                mView.sendVerifyCodeSuccess();
            }
        });
    }

    @Override
    public void resetPassword(String country, String phone, String verifyCode, String password) {
        UMSSDK.resetPasswordWithPhoneNumber(country,phone,verifyCode,password, new UMSCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        super.onSuccess(aVoid);
                        mView.resetPasswordSuccess();
                    }
                });
    }
}
