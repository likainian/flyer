package com.flyer.chat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.activity.account.LoginActivity;
import com.flyer.chat.activity.account.AccountManager;
import com.flyer.chat.activity.setting.AboutActivity;
import com.flyer.chat.activity.setting.FeedBackActivity;
import com.flyer.chat.activity.setting.UserHeadActivity;
import com.flyer.chat.activity.setting.UserInfoActivity;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseFragment;


import cn.bmob.v3.BmobUser;
import com.flyer.chat.share.ShareSDK;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by mike.li on 2018/7/9.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, MeContract.MeView {
    public ImageView mHeadBack;
    public ImageView mHead;
    public TextView mAppVersion;
    public TextView mName;
    public LinearLayout mMeInfo;
    public LinearLayout mShareChat;
    public LinearLayout mFeedBack;
    public LinearLayout mUpData;
    public LinearLayout mAboutChat;

    private BroadcastReceiver avatarBroadcastReceiver;
    private MePresenter mPresenter;

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
        mPresenter = new MePresenter(this);
        initView(view);
        mPresenter.getLoginUserAvatar();
        avatarBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getLoginUserAvatar();
            }
        };
        if(getActivity()!=null){
            getActivity().registerReceiver(avatarBroadcastReceiver,new IntentFilter(UserInfoActivity.ACTION_USER));
        }
    }

    @Override
    public void onDestroy() {
        if(getActivity()!=null){
            getActivity().unregisterReceiver(avatarBroadcastReceiver);
        }
        super.onDestroy();
    }

    private void setHead() {
    }

    private void initView(View rootView) {
        this.mHeadBack = rootView.findViewById(R.id.head_back);
        this.mHead = rootView.findViewById(R.id.head);
        this.mName = rootView.findViewById(R.id.name);
        this.mAppVersion = rootView.findViewById(R.id.app_version);
        this.mMeInfo = rootView.findViewById(R.id.me_info);
        this.mShareChat = rootView.findViewById(R.id.share_chat);
        this.mFeedBack = rootView.findViewById(R.id.feed_back);
        this.mUpData = rootView.findViewById(R.id.up_data);
        this.mAboutChat = rootView.findViewById(R.id.about_chat);
        TextView mBtnLogout = rootView.findViewById(R.id.btn_logout);
        mHeadBack.setOnClickListener(this);
        mMeInfo.setOnClickListener(this);
        mShareChat.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mUpData.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        mAboutChat.setOnClickListener(this);
        mAppVersion.setText("版本更新(V"+ BuildConfig.VERSION_NAME+")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_info:
                UserInfoActivity.startActivity(getActivity());
                break;
            case R.id.head_back:
                UserHeadActivity.startActivity(getActivity());
                break;
            case R.id.btn_logout:
                BmobUser.logOut();
                LoginActivity.startActivity(getActivity());
                mPresenter.logout();
                break;
            case R.id.share_chat:
                ShareSDK.showShare(ChatApplication.getInstance());
                break;
            case R.id.about_chat:
                AboutActivity.startActivity(getActivity());
                break;
            case R.id.feed_back:
                FeedBackActivity.startActivity(getActivity());
                break;
            case R.id.up_data:
                //todo
                break;
        }
    }

    @Override
    public void setLoginUserAvatar(Bitmap bitmap) {
        mHead.setImageBitmap(bitmap);
        Blurry.with(getActivity()).sampling(1).async().from(bitmap).into(mHeadBack);
    }

    @Override
    public void setLoginUserNick(String nickName) {
        mName.setText(nickName);
    }

    @Override
    public void logoutSuccess() {
        AccountManager.logout();
        LoginActivity.startActivity(getActivity());
    }
}
