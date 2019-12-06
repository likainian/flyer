package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.MediaActivity;
import com.flyer.chat.util.BitmapUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.PhotoView;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/8/23.
 */

public class UserHeadActivity extends MediaActivity implements View.OnClickListener {
    private PhotoView mPhotoView;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserHeadActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_head);
        initView();
        setHead();
    }

    @Override
    protected void takePictureReturn(final ArrayList<String> imgPathList) {
        showLoadingDialog("正在上传头像");
        JMessageClient.updateUserAvatar(BitmapUtil.getCompressImg(imgPathList.get(0)), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                closeLoadingDialog();
                LogUtil.i(i+s);
                if(i==0){
                    ToastUtil.showToast("修改头像成功");
                    sendBroadcast(new Intent(UserInfoActivity.ACTION_USER));
                    setHead();
                }else {
                    ToastUtil.showToast("上传头像失败");
                    imgPathList.clear();
                }
            }
        });
    }

    private void setHead() {
        UserInfo myInfo = JMessageClient.getMyInfo();
        myInfo.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                LogUtil.i(i + s);
                imgPathList.clear();
                if (i == 0) {
                    mPhotoView.setImageBitmap(bitmap);
                } else {
                    ToastUtil.showToast("下载头像失败");
                }
            }
        });
    }

    private void initView() {
        ImageView mToolbarLeft = findViewById(R.id.toolbar_left);
        TextView mToolbarMiddle = findViewById(R.id.toolbar_middle);
        TextView mToolbarRight = findViewById(R.id.toolbar_right);
        mPhotoView = findViewById(R.id.photo_view);
        mToolbarLeft.setOnClickListener(this);
        mToolbarMiddle.setText("个人头像");
        mToolbarRight.setText("修改");
        mToolbarRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.toolbar_right:
                showPictureDialog(1);
                break;
        }
    }
}
