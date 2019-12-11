package com.flyer.chat.fragment;

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
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

/**
 * Created by mike.li on 2018/11/6.
 */

public class MePresenter implements MeContract.MePresenter {
    private MeContract.MeView mView;

    public MePresenter(MeContract.MeView mView) {
        this.mView = mView;
    }
    @Override
    public void getLoginUserAvatar() {
        UMSSDK.getLoginUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User o) {
                super.onSuccess(o);
                String[] strings = o.avatar.get();
                String nickName = o.nickname.get();
                mView.setLoginUserNick(nickName);
                if(strings==null){
                    BitmapDrawable drawable = (BitmapDrawable)CommonUtil.getDrawable(R.drawable.default_head);
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
        });
    }

    @Override
    public void logout() {
        UMSSDK.logout(new UMSCallback<Void>(){
            @Override
            public void onSuccess(Void o) {
                super.onSuccess(o);
                mView.logoutSuccess();
            }
        });
    }
}
