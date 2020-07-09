package com.flyer.chat.activity.common;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ScanActivity extends ToolbarActivity {

    public static void startActivity(final FragmentActivity context){
        Disposable subscribe = new RxPermissions(context).request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            context.startActivity(new Intent(context,ScanActivity.class));
                        } else {
                            ToastUtil.showToast("请开启相机权限");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable);
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

}
