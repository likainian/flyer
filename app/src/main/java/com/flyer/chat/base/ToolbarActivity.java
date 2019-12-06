package com.flyer.chat.base;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;


public abstract class ToolbarActivity extends BaseActivity{
    private FrameLayout mToolbarLeft;
    private TextView mToolbarMiddle;
    private FrameLayout mToolbarRight;
    private TextView mToolbarRightText;
    private ImageView mToolbarRightImage;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initToolbarView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbarView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initToolbarView();
    }

    private void initToolbarView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mToolbarRightText = findViewById(R.id.toolbar_right_text);
        mToolbarRightImage = findViewById(R.id.toolbar_right_image);
        if(mToolbarLeft!=null)mToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setToolbarMiddleText(String title){
        if(mToolbarMiddle!=null)mToolbarMiddle.setText(title);
    }
    public void setToolbarRightText(String menu){
        if(mToolbarRightText!=null)mToolbarRightText.setText(menu);
    }
    public void setToolbarRightImage(@DrawableRes int resId){
        if(mToolbarRightImage!=null)mToolbarRightImage.setImageResource(resId);
    }
    public void setToolbarLeftClick(View.OnClickListener onClickListener){
        if(mToolbarLeft!=null)mToolbarLeft.setOnClickListener(onClickListener);
    }
    public void setToolbarRightClick(View.OnClickListener onClickListener){
        if(mToolbarRight!=null)mToolbarRight.setOnClickListener(onClickListener);
    }
}
