package com.flyer.chat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.flyer.chat.activity.BigPictureActivity;
import com.flyer.chat.activity.account.LoginActivity;
import com.flyer.chat.activity.setting.AboutActivity;
import com.flyer.chat.activity.setting.UserInfoActivity;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.dialog.ShareDialog;
import com.flyer.chat.util.DeviceUtil;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.jpush.im.android.api.model.UserInfo;

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
    public LinearLayout mLlUpData;
    public LinearLayout mLlAbout;
    private TextView mTvLogout;
    private UserInfo myInfo;
    private BroadcastReceiver avatarBroadcastReceiver;
    private TextView mTvVersion;

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
        setHead();
        avatarBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setHead();
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
        this.mMeInfo = rootView.findViewById(R.id.me_info);
        this.mShareChat = rootView.findViewById(R.id.share_chat);
        this.mFeedBack = rootView.findViewById(R.id.feed_back);
        this.mLlUpData = rootView.findViewById(R.id.ll_up_data);
        this.mLlAbout = rootView.findViewById(R.id.ll_about);
        this.mTvVersion = rootView.findViewById(R.id.tv_version);
        mTvLogout = rootView.findViewById(R.id.tv_logout);
        mHeadBack.setOnClickListener(this);
        mMeInfo.setOnClickListener(this);
        mShareChat.setOnClickListener(this);
        mLlAbout.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);

        mTvVersion.setText("v"+ BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_info:
                UserInfoActivity.startActivity(getActivity());
                break;
            case R.id.head_back:
                ArrayList<String> list = new ArrayList<>();
                list.add(DeviceUtil.getSavePicturePath("avatar"));
                BigPictureActivity.startActivity(getActivity(),list,0);
                break;
            case R.id.ll_about:
                AboutActivity.startActivity(getActivity());
                break;
            case R.id.tv_logout:
                BmobUser.logOut();
                LoginActivity.startActivity(getActivity());
                break;
            case R.id.share_chat:
                new ShareDialog(getActivity()).show();
                break;
        }
    }
}
