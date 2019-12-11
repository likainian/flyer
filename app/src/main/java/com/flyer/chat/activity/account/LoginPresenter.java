package com.flyer.chat.activity.account;

import com.flyer.chat.network.UMSCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

/**
 * Created by mike.li on 2018/11/5.
 */

public class LoginPresenter implements LoginContract.LoginPresenter{
    private LoginContract.LoginView mView;

    public LoginPresenter(LoginContract.LoginView mView) {
        this.mView = mView;
    }


    @Override
    public void login(String country, String phone, String password) {
        mView.showLoadingDialog();
        UMSSDK.loginWithPhoneNumber(country,phone,password,new UMSCallback<User>(){
            @Override
            public void onSuccess(User user) {
                mView.closeLoadingDialog();
                mView.loginSuccess(user);
                super.onSuccess(user);
            }

            @Override
            public void onFailed(Throwable throwable) {
                mView.closeLoadingDialog();
                super.onFailed(throwable);
            }

            @Override
            public void onCancel() {
                mView.closeLoadingDialog();
                super.onCancel();
            }
        });
    }
}
