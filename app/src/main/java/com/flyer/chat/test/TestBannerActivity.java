package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.tmall.ultraviewpager.UltraViewPager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/28.
 */
public class TestBannerActivity extends ToolbarActivity {
    private Banner mBanner;
    private ArrayList<Integer> carousels;
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestBannerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_banner);
        setToolbarMiddleText("轮播图");
        carousels = new ArrayList<>();
        carousels.add(R.drawable.banner1);
        carousels.add(R.drawable.banner2);
        carousels.add(R.drawable.banner3);

        mBanner = (Banner) findViewById(R.id.banner);
        showBanner();

        UltraViewPager ultraViewPager = (UltraViewPager)findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new UltraPagerAdapter();
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.8f);
        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.setAutoScroll(2000);
    }

    //轮播图参数
    private void showBanner() {
        mBanner.setImageLoader(new BannerLoader());
        mBanner.setImages(carousels);
        mBanner.setDelayTime(2000);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.start();
    }
    //轮播图适配器
    private class BannerLoader extends ImageLoader {

        @Override
        public void displayImage(final Context context, Object resourceId, final ImageView imageView) {
            Glide.with(TestBannerActivity.this).load(resourceId).into(imageView);
        }
    }

    private class UltraPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return carousels.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_view, null);
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.iv_image);
            Glide.with(TestBannerActivity.this).load(carousels.get(position)).into(imageView);
            container.addView(linearLayout);
            return linearLayout;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            LinearLayout view = (LinearLayout) object;
            container.removeView(view);
        }
    }
}
