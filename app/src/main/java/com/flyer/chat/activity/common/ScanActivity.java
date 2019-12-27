package com.flyer.chat.activity.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastUtil;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ScanActivity extends ToolbarActivity {
    private CameraManager cameraManager;
    private DecoratedBarcodeView mDBV;

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
        initView();
        initDecode(savedInstanceState);
    }

    private void initView() {
        setToolbarMiddleText("扫一扫");
        mDBV = findViewById(R.id.dbv_custom);
        TextView statusView = mDBV.getStatusView();
        if(statusView!=null){
            statusView.setGravity(Gravity.CENTER);
            statusView.setPadding(0,0,0,100);
        }
    }

    private void initDecode(Bundle savedInstanceState) {
        //重要代码，初始化捕获
        cameraManager = new CameraManager(this, mDBV);
        cameraManager.initializeFromIntent(getIntent(),savedInstanceState);
        cameraManager.decode();
    }



    @Override
    protected void onPause() {
        super.onPause();
        cameraManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        cameraManager.onSaveInstanceState(outState);
    }

    private class CameraManager extends CaptureManager {
        private CameraManager(Activity activity, DecoratedBarcodeView barcodeView) {
            super(activity, barcodeView);
        }

        @Override
        protected void returnResult(BarcodeResult rawResult) {
            LogUtil.i("ttt",rawResult.getText());
            ScanActivity.this.finish();
        }

    }

}
