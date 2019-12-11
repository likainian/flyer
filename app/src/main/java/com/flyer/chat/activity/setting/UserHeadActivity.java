package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.MediaActivity;
import com.flyer.chat.widget.PhotoView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mike.li on 2018/8/23.
 */

public class UserHeadActivity extends MediaActivity implements View.OnClickListener, UserHeadContract.UserHeadView {
    private PhotoView mPhotoView;

    private UserHeadPresenter mPresenter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserHeadActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_head);
        mPresenter = new UserHeadPresenter(this);
        initView();
        mPresenter.getLoginUserAvatar();
    }

    @Override
    protected void takePictureReturn(final ArrayList<String> imgPathList) {
        String filePath = imgPathList.get(0);
        mPresenter.uploadAvatar(new File(filePath));
    }

    private void initView() {
        FrameLayout mToolbarLeft = findViewById(R.id.toolbar_left);
        TextView mToolbarRight = findViewById(R.id.toolbar_right);
        mPhotoView = findViewById(R.id.photo_view);
        mToolbarLeft.setOnClickListener(this);
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

    @Override
    public void setLoginUserAvatar(Bitmap bitmap) {
        mPhotoView.setImageBitmap(bitmap);
    }
}
