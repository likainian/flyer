package com.flyer.chat.activity.setting;

import android.graphics.Bitmap;

import com.flyer.chat.base.BaseContract;

import java.io.File;

/**
 * Created by mike.li on 2018/11/6.
 */

public class UserHeadContract {
    interface UserHeadView extends BaseContract.BaseView{
        void setLoginUserAvatar(Bitmap bitmap);
    }
    interface UserHeadPresenter extends BaseContract.BasePresenter{
        void uploadAvatar(File file);
        void getLoginUserAvatar();
    }
}
