package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.widget.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike.li on 2018/8/20.
 */

public class BigPictureActivity extends BaseActivity {
    private ViewPager mPager;
    private TextView mTvCount;

    public static void startActivity(Context context, List<String> imgPathList, int imgIndex) {
        if (context == null || CheckUtil.isEmpty(imgPathList)) return;
        Intent intent = new Intent(context, BigPictureActivity.class);
        intent.putStringArrayListExtra("imgPath", (ArrayList<String>) imgPathList);
        intent.putExtra("imgIndex", imgIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        ArrayList<String> imgPath = getIntent().getStringArrayListExtra("imgPath");
        int imgIndex = getIntent().getIntExtra("imgIndex", 0);
        initView();
        mPager.setAdapter(new PicturePagerAdapter(imgPath));
        mPager.setCurrentItem(imgIndex);
    }

    private void initView() {
        mPager = findViewById(R.id.pager);
        mTvCount = findViewById(R.id.tv_count);
        FrameLayout mFlLeft = findViewById(R.id.fl_left);
        mFlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private class PicturePagerAdapter extends PagerAdapter{
        private ArrayList<String> imgPathList;

        public PicturePagerAdapter(ArrayList<String> imgPathList) {
            this.imgPathList = imgPathList;
        }

        @Override
        public int getCount() {
            return imgPathList==null?0:imgPathList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView view = new PhotoView(ChatApplication.getInstance().getApplicationContext());
            if(imgPathList.get(position).startsWith("http")){
                RequestOptions options = new RequestOptions().placeholder(R.drawable.default_image).error(R.drawable.default_image);
                Glide.with(BigPictureActivity.this).applyDefaultRequestOptions(options)
                        .load(imgPathList.get(position)).into(view);
            }else {
                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.default_image).error(R.drawable.default_image);
                Glide.with(BigPictureActivity.this).applyDefaultRequestOptions(options)
                        .load(new File(imgPathList.get(position))).into(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            super.finishUpdate(container);
            mTvCount.setText(String.format("%d/%d", mPager.getCurrentItem() + 1, imgPathList.size()));
        }
    }
}
