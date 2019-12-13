package com.flyer.chat.activity.setting;

import java.io.File;
import java.util.HashMap;

/**
 * Created by mike.li on 2018/11/6.
 */

public class UserHeadPresenter implements UserHeadContract.UserHeadPresenter {
    private UserHeadContract.UserHeadView mView;

    public UserHeadPresenter(UserHeadContract.UserHeadView mView) {
        this.mView = mView;
    }

    @Override
    public void uploadAvatar(File file) {
        mView.showLoadingDialog("正在上传头像");
    }
    private void updateAvatar(HashMap<String, Object> map){
    }

    @Override
    public void getLoginUserAvatar() {
    }
}
