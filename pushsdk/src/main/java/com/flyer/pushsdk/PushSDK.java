package com.flyer.pushsdk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.igexin.sdk.PushManager;

/**
 * Created by mike.li on 2018/5/2.
 */

public class PushSDK implements PushFace{
    private PushSDK() {
    }
    private static PushSDK instance;
    public synchronized static PushSDK getInstance() {
        if(instance==null){
            instance = new PushSDK();
        }
        return instance;
    }

    @Override
    public void init(Application application) {
        PushManager.getInstance().initialize(application, GeTuiPushService.class);
        PushManager.getInstance().registerPushIntentService(application, GeTuiIntentService.class);
    }

    @Override
    public void stopService(Application application) {
        PushManager.getInstance().stopService(application);
    }

    void sendMessage(Context context, String data){
        Intent intent = new Intent(context.getPackageName()+".PUSH");
        intent.putExtra("data",data);
        context.sendBroadcast(intent);
    }
}
