package com.flyer.chat.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,HomeActivity.class));
    }
    private TabLayout mHomeTab;
    private List<Fragment> fragmentList;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChatApplication.getInstance().clearAllActivities();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHomeTab = findViewById(R.id.home_tab);
        initPager();
        initTab();
       initSwitch();

//        new  PgyUpdateManager.Builder().setUpdateManagerListener(new UpdateManagerListener() {
//            @Override
//            public void onNoUpdateAvailable() {
//                LogUtil.i("ttt","没有更新");
//            }
//
//            @Override
//            public void onUpdateAvailable(AppBean appBean) {
//                LogUtil.i("ttt","有更新"+appBean.getReleaseNote()+appBean.getDownloadURL());
//            }
//
//            @Override
//            public void checkUpdateFailed(Exception e) {
//                LogUtil.i("ttt","异常"+e.getMessage());
//            }
//        }).register();
        /** 旧版本修改 **/
        PgyUpdateManager.register(new UpdateManagerListener() {   // 弃用方法，不推介
            @Override
            public void onNoUpdateAvailable() {
                //检测没有跟新的回调
                Log.d("pgyer", "onNoUpdateAvailable");
            }

            @Override
            public void onUpdateAvailable(AppBean appBean) {
                //检测有更新的回调
                Log.d("pgyer", "there is new version can update"
                        + "new versionCode is " + appBean.getVersionCode());
                //调用以下方法，DownloadFileListener 才有效；
                //如果完全使用自己的下载方法，不需要设置DownloadFileListener
                PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
            }

            @Override
            public void checkUpdateFailed(Exception e) {   //再回调失败的时候，增加了新的接口
                //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                Log.d("pgyer", "checkUpdateFailed"+e.getMessage());
            }
        });
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
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_chat,R.drawable.selector_home_tab_chat)));
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_note,R.drawable.selector_home_tab_note)));
        mHomeTab.addTab(mHomeTab.newTab().setCustomView(getItemView(R.string.text_me,R.drawable.selector_home_tab_me)));
    }

    private View getItemView(@StringRes int title, @DrawableRes int icon){
        View item = LayoutInflater.from(this).inflate(R.layout.view_home_tab_layout, null);
        TextView tv_item_title = item.findViewById(R.id.tv_item_title);
        tv_item_title.setText(title);
        ImageView iv_tab_icon = item.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(icon);
        return item;
    }

    private void initPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(NearFragment.newInstance());
        fragmentList.add(ChatFragment.newInstance());
        fragmentList.add(ChatFragment.newInstance());
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
            Toast.makeText(this.getApplication(), "再按一次退出程序", Toast.LENGTH_LONG).show();
            Disposable subscribe = Observable.just(isExit).subscribeOn(Schedulers.computation()).delay(2, TimeUnit.SECONDS).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean){
                    isExit = false;
                }
            });
        } else {
            finish();
        }
    }
}
