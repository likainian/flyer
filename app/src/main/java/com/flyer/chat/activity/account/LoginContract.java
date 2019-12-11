package com.flyer.chat.activity.account;

import com.flyer.chat.base.BaseContract;
import com.mob.ums.User;

/**
 * Created by mike.li on 2018/11/5.
 */

public class LoginContract {
    interface LoginView extends BaseContract.BaseView{
        void loginSuccess(User user);
    }
    interface LoginPresenter extends BaseContract.BasePresenter{
        void login(String country, String phone, String password);
    }
}
