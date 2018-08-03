package com.flyer.pushsdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.igexin.sdk.GTServiceManager;

/**
 * Created by mike.li on 2018/5/2.
 */

public class GeTuiPushService extends Service {

    public static final String TAG = GeTuiPushService.class.getName();

    @Override
    public void onCreate() {
        Logger.i( "GeTuiPushService -> onCreate");
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("GeTuiPushService -> onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("GeTuiPushService -> onBind");
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        Logger.i("GeTuiPushService -> onDestroy");
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
