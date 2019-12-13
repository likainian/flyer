package com.flyer.chat.activity.setting;

/**
 * Created by mike.li on 2018/11/6.
 */

public class UserInfoPresenter implements UserInfoContract.UserInfoPresenter {
    private UserInfoContract.UserInfoView mView;

    public UserInfoPresenter(UserInfoContract.UserInfoView mView) {
        this.mView = mView;
    }

    @Override
    public void getUserInfo() {
    }
}
