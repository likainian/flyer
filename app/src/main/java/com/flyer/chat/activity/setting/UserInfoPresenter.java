package com.flyer.chat.activity.setting;

import com.flyer.chat.network.UMSCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

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
        UMSSDK.getLoginUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User user) {
                mView.showUserInfo(user);
            }
        });
    }
}
