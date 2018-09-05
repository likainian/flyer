package com.flyer.chat.activity.setting;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ScanActivity extends BaseActivity{
    DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mDBV = findViewById(R.id.dbv_custom);
        //重要代码，初始化捕获
        captureManager = new CaptureManager(this,mDBV);
        captureManager.initializeFromIntent(getIntent(),savedInstanceState);
        captureManager.decode();
    }

}
