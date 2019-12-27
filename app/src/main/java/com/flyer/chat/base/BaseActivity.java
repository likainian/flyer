package com.flyer.chat.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.dialog.LoadingDialog;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.gyf.barlibrary.ImmersionBar;

public abstract class BaseActivity extends AppCompatActivity{

    private LoadingDialog loadingDialog;
    public ImmersionBar mImmersionBar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(BaseContextWrapper.wrap(newBase, SharedPreferencesUtil.getInstance().getLanguage()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatApplication.getInstance().addActivity(this);
        //适配状态栏黑色字体，不能改变这加入0.5透明度遮盖
        mImmersionBar  = ImmersionBar.with(this);
    }

    @Override
    protected void onDestroy() {
        ChatApplication.getInstance().removeActivity(this);
        if (mImmersionBar != null)mImmersionBar.destroy();
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mImmersionBar.statusBarDarkFont(true,0).statusBarColor(R.color.colorPrimary).init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mImmersionBar.statusBarDarkFont(true,0).statusBarColor(R.color.colorPrimary).init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mImmersionBar.statusBarDarkFont(true,0).statusBarColor(R.color.colorPrimary).init();
    }

    protected void addFragment(@IdRes int layoutFragmentId, Fragment fragment) {
        if (null == fragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction().add(layoutFragmentId, fragment).commit();
    }

    public void showLoadingDialog() {
        showLoadingDialog(CommonUtil.getString(R.string.text_loading));
    }

    public void showLoadingDialog(String message) {
        loadingDialog = new LoadingDialog(this).setMessage(message);
        loadingDialog.show();
    }

    public void closeLoadingDialog() {
        if(loadingDialog!= null){
            loadingDialog.dismiss();
        }
    }
}
