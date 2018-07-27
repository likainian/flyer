package com.flyer.pushsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * Created by mike.li on 2018/5/2.
 */

public class GeTuiIntentService extends GTIntentService {
    public static final String TAG = GeTuiIntentService.class.getName();

    @Override
    public void onReceiveServicePid(Context context, int i) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onReceiveServicePid: "+i);
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        if(BuildConfig.DEBUG) Log.d(TAG, "onReceiveClientId: "+s);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("channnelId_BaiDu", s);
        editor.putString("userId_BaiDu", "GETUI");
        editor.apply();
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onReceiveMessageData: "+gtTransmitMessage);
        byte[] payload = gtTransmitMessage.getPayload();
        if (payload == null) {
            if (BuildConfig.DEBUG) Log.d(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            if (BuildConfig.DEBUG)
                Log.d(TAG,"个推 推送数据：" + data);
            PushSDK.getInstance().sendMessage(context,data);
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onReceiveOnlineState: "+b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onReceiveCommandResult: "+gtCmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onNotificationMessageArrived: "+gtNotificationMessage);
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onNotificationMessageClicked: "+gtNotificationMessage);
    }
}
