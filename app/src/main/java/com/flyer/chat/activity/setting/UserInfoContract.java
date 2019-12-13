package com.flyer.chat.activity.setting;

import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.base.BaseContract;

/**
 * Created by mike.li on 2018/11/6.
 */

public class UserInfoContract {
    interface UserInfoView extends BaseContract.BaseView{
        void showUserInfo(User user);
    }
    interface UserInfoPresenter extends BaseContract.BasePresenter{
        void getUserInfo();
    }
}
