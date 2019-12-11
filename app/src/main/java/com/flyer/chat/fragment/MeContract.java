package com.flyer.chat.fragment;

import android.graphics.Bitmap;

import com.flyer.chat.base.BaseContract;

/**
 * Created by mike.li on 2018/11/6.
 */

public class MeContract {
    interface MeView extends BaseContract.BaseView{
        void setLoginUserAvatar(Bitmap bitmap);
        void setLoginUserNick(String nickName);
        void logoutSuccess();
    }
    interface MePresenter extends BaseContract.BasePresenter{
        void getLoginUserAvatar();
        void logout();
    }
}
