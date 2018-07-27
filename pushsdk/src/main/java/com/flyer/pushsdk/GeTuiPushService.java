package com.flyer.pushsdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.igexin.sdk.GTServiceManager;

/**
 * Created by mike.li on 2018/5/2.
 */

public class GeTuiPushService extends Service {

    public static final String TAG = GeTuiPushService.class.getName();

    @Override
    public void onCreate() {
        // 该行日志在 release 版本去掉
        if (BuildConfig.DEBUG)
            Log.d(TAG, TAG + "GeTuiPushService -> onCreate");
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 该行日志在 release 版本去掉
        if (BuildConfig.DEBUG)
            Log.d(TAG, TAG + "GeTuiPushService -> onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 该行日志在 release 版本去掉
        if (BuildConfig.DEBUG)
            Log.d(TAG, "GeTuiPushService -> onBind");
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        // 该行日志在 release 版本去掉
        if (BuildConfig.DEBUG)
            Log.d(TAG, "GeTuiPushService -> onDestroy");
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
