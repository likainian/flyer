package com.flyer.chat.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyer.chat.R;
import com.flyer.chat.activity.LoginActivity;
import com.flyer.chat.activity.UserInfoActivity;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.SharedPreferencesHelper;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by mike.li on 2018/7/9.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    public ImageView mHeadBack;
    public ImageView mHead;
    public TextView mName;
    public LinearLayout mMeInfo;
    public LinearLayout mShareChat;
    public LinearLayout mFeedBack;
    public LinearLayout mUpData;
    public LinearLayout mAboutChat;
    private TextView mBtnLogout;
    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initData() {
        UserInfo myInfo = JMessageClient.getMyInfo();
        Glide.with(this).applyDefaultRequestOptions(GlideOptions.UserOptions()).asBitmap().
                load("").into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mHead.setImageBitmap(resource);
                Blurry.with(getActivity()).sampling(1).async().from(resource).into(mHeadBack);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
                mHead.setImageBitmap(bitmap);
                Blurry.with(getActivity()).sampling(1).async().from(bitmap).into(mHeadBack);
            }
        });
    }

    private void initView(View rootView) {
        this.mHeadBack = rootView.findViewById(R.id.head_back);
        this.mHead = rootView.findViewById(R.id.head);
        this.mName = rootView.findViewById(R.id.name);
        this.mMeInfo = rootView.findViewById(R.id.me_info);
        this.mShareChat = rootView.findViewById(R.id.share_chat);
        this.mFeedBack = rootView.findViewById(R.id.feed_back);
        this.mUpData = rootView.findViewById(R.id.up_data);
        this.mAboutChat = rootView.findViewById(R.id.about_chat);
        mBtnLogout = rootView.findViewById(R.id.btn_logout);
        mMeInfo.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_info:
                UserInfoActivity.startActivity(getActivity());
                break;
            case R.id.btn_logout:
                SharedPreferencesHelper.getInstance().setUserName("");
                SharedPreferencesHelper.getInstance().setPassWord("");
                JMessageClient.logout();
                LoginActivity.startActivity(getActivity());
                break;
        }
    }
}
