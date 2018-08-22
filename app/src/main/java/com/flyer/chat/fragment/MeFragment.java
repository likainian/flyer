package com.flyer.chat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.BigPictureActivity;
import com.flyer.chat.activity.LoginActivity;
import com.flyer.chat.activity.setting.UserInfoActivity;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.FileUtil;
import com.flyer.chat.util.SharedPreferencesHelper;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
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
    private UserInfo myInfo;
    private BroadcastReceiver avatarBroadcastReceiver;

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
        myInfo = JMessageClient.getMyInfo();
        myInfo.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0){
                    FileUtil.saveCacheBitmap("avatar",bitmap);
                    mHead.setImageBitmap(bitmap);
                    Blurry.with(getActivity()).sampling(1).async().from(bitmap).into(mHeadBack);
                }else {
                    Bitmap bitmapD = BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
                    mHead.setImageBitmap(bitmapD);
                    Blurry.with(getActivity()).sampling(1).async().from(bitmapD).into(mHeadBack);
                }
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
        mHeadBack.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
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
            case R.id.btn_logout:
                SharedPreferencesHelper.getInstance().setUserName("");
                SharedPreferencesHelper.getInstance().setPassWord("");
                JMessageClient.logout();
                LoginActivity.startActivity(getActivity());
                break;
        }
    }
}
