package com.flyer.chat.activity.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.network.UMSCallback;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastUtil;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

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
        UMSSDK.uploadAvatar(file.getAbsolutePath(),new UMSCallback<HashMap<String, Object>>(){
            @Override
            public void onSuccess(HashMap<String, Object> stringObjectHashMap) {
                super.onSuccess(stringObjectHashMap);
                HashMap<String, Object> map = new HashMap<>();
                map.put("avatarId",stringObjectHashMap.get("id"));
                updateAvatar(map);
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mView.closeLoadingDialog();
            }
        });
    }
    private void updateAvatar(HashMap<String, Object> map){
        UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                ToastUtil.showToast("修改头像成功");
                ChatApplication.getInstance().sendBroadcast(new Intent(UserInfoActivity.ACTION_USER));
                getLoginUserAvatar();
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mView.closeLoadingDialog();
            }
        });
    }

    @Override
    public void getLoginUserAvatar() {
        UMSSDK.getLoginUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User o) {
                super.onSuccess(o);
                mView.closeLoadingDialog();
                LogUtil.i("um",o.toString());
                String[] strings = o.avatar.get();
                if(strings==null){
                    BitmapDrawable drawable = (BitmapDrawable) CommonUtil.getDrawable(R.drawable.default_head);
                    mView.setLoginUserAvatar(drawable.getBitmap());
                }else {
                    Glide.with(ChatApplication.getInstance().getApplicationContext()).applyDefaultRequestOptions(GlideOptions.UserOptions()).asBitmap().load(strings[0]).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mView.setLoginUserAvatar(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            BitmapDrawable drawable = (BitmapDrawable)CommonUtil.getDrawable(R.drawable.default_head);
                            mView.setLoginUserAvatar(drawable.getBitmap());
                        }
                    });
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mView.closeLoadingDialog();
            }
        });
    }
}
