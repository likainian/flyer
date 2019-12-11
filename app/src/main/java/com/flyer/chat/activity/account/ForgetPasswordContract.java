package com.flyer.chat.activity.account;

import com.flyer.chat.base.BaseContract;

/**
 * Created by mike.li on 2018/11/5.
 */

public class ForgetPasswordContract {
    interface ForgetPasswordView extends BaseContract.BaseView{
        void sendVerifyCodeSuccess();
        void resetPasswordSuccess();
    }
    interface ForgetPasswordPresenter extends BaseContract.BasePresenter{
        void sendVerifyCode(String country,String mobileNo);
        void resetPassword(String country, String phone, String verifyCode, String password);
    }
}
