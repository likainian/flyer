package com.flyer.chat.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.fragment.ChatFragment;
import com.flyer.chat.fragment.HomeFragment;
import com.flyer.chat.fragment.MeFragment;
import com.flyer.chat.fragment.NoteFragment;
import com.flyer.chat.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private TabLayout mHomeTab;
    private List<Fragment> fragmentList;
    private boolean isExit;

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        ChatApplication.updateUser();
        JAnalyticsInterface.onEvent(this,new CountEvent("打开主界面").addKeyValue("key","value"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHomeTab = findViewById(R.id.home_tab);
        initPager();
        initTab();
        initSwitch();
        initUser();
    }

    private void initUser() {

    }

    private void initSwitch() {
        mHomeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction().show(fragmentList.get(tab.getPosition())).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction().hide(fragmentList.get(tab.getPosition())).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initTab() {
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_home,R.drawable.selector_home_tab_home)));
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_chat,R.drawable.selector_home_tab_home)));
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_note,R.drawable.selector_home_tab_home)));
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_me,R.drawable.selector_home_tab_home)));
    }

    private View getItemView(@StringRes int title, @DrawableRes int icon){
        View item = LayoutInflater.from(this).inflate(R.layout.home_page_tab_item, null);
        TextView tv_item_title = item.findViewById(R.id.tv_item_title);
        tv_item_title.setText(title);
        ImageView iv_tab_icon = item.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(icon);
        return item;
    }

    private void initPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(ChatFragment.newInstance());
        fragmentList.add(NoteFragment.newInstance());
        fragmentList.add(MeFragment.newInstance());
        for (Fragment fragment:fragmentList){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,fragment).hide(fragment).commit();
        }
        getSupportFragmentManager().beginTransaction().show(fragmentList.get(0)).commit();
    }

    @Override
    public void onBackPressed() {
        if (!this.isExit) {
            this.isExit = true;
            Toast.makeText(this.getApplication(), CommonUtil.getString(R.string.toast_exit_app_press_again), Toast.LENGTH_LONG).show();
            Observable.just(isExit).subscribeOn(Schedulers.computation()).delay(2, TimeUnit.SECONDS).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    isExit = false;
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
