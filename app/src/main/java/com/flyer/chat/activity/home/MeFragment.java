package com.flyer.chat.activity.home;

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

import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.activity.setting.UserInfoActivity;
import com.flyer.chat.activity.account.LoginActivity;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.activity.setting.AboutActivity;
import com.flyer.chat.activity.setting.FeedBackActivity;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.BitmapUtil;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.GlideEngine;
import com.flyer.chat.util.ToastUtil;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by mike.li on 2018/7/9.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    public ImageView mIvHeadBack;
    public ImageView mIvHead;
    public TextView mTvName;
    public LinearLayout mLlInfo;
    public LinearLayout mLlShareChat;
    public LinearLayout mLlFeedBack;
    public LinearLayout mLlUpData;
    public LinearLayout mLlAbout;

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

    }

    @Override
    public void onResume() {
        super.onResume();
        showHead();
    }

    private void initView(View rootView) {
        this.mIvHeadBack = rootView.findViewById(R.id.iv_head_back);
        this.mIvHead = rootView.findViewById(R.id.iv_head);
        this.mTvName = rootView.findViewById(R.id.tv_name);
        this.mLlInfo = rootView.findViewById(R.id.ll_info);
        this.mLlShareChat = rootView.findViewById(R.id.ll_share_chat);
        this.mLlFeedBack = rootView.findViewById(R.id.ll_feed_back);
        this.mLlUpData = rootView.findViewById(R.id.ll_up_data);
        this.mLlAbout = rootView.findViewById(R.id.ll_about);
        TextView mTvVersion = rootView.findViewById(R.id.tv_version);
        TextView mTvLogout = rootView.findViewById(R.id.tv_logout);
        mIvHeadBack.setOnClickListener(this);
        mLlInfo.setOnClickListener(this);
        mLlShareChat.setOnClickListener(this);
        mLlFeedBack.setOnClickListener(this);
        mLlAbout.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);

        mTvVersion.setText("v"+ BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_info:
                UserInfoActivity.startActivity(getActivity());
                break;
            case R.id.iv_head_back:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("cn.bmob.v3.util.BmobContentProvider")
                        .setCameraLocation(Setting.LIST_FIRST)
                        .setCount(1)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                                upLoadHead(paths.get(0));
                            }
                        });

                break;
            case R.id.ll_about:
                AboutActivity.startActivity(getActivity());
                break;
            case R.id.ll_feed_back:
                FeedBackActivity.startActivity(getActivity());
                break;
            case R.id.tv_logout:
                BmobUser.logOut();
                LoginActivity.startActivity(getActivity());
                break;
            case R.id.ll_share_chat:
                break;
        }
    }

    private void upLoadHead(String file){
        showLoadingDialog();
        String image = BitmapUtil.bitmapToBase64(BitmapFactory.decodeFile(file));
        final User user = BmobUser.getCurrentUser(User.class);
        user.setImg(image);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                closeLoadingDialog();
                if (e == null) {
                    ToastUtil.showToast("更新用户信息成功");
                    showHead();
                } else {
                    ToastUtil.showToast("更新用户信息失败：" + e.getMessage());
                }
            }
        });
    }

    private void showHead(){
        User user = BmobUser.getCurrentUser(User.class);
        RetrofitService.getInstance().downloadImage("https://www.ihr360.com/gateway/api/image/9a205dd0-2ec0-4796-a805-5d9d5406c362.jpg", new CallBack<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if(bitmap!=null){
                    mIvHead.setImageBitmap(bitmap);
                    Blurry.with(getActivity()).sampling(1).async().from(bitmap).into(mIvHeadBack);
                }
            }
        });

        mTvName.setText(user.getMobilePhoneNumber());
        if(CheckUtil.isNotEmpty(user.getNikeName())){
            mTvName.append("("+user.getNikeName()+")");
        }
    }


}
