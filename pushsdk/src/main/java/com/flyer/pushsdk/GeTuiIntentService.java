package com.flyer.pushsdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * Created by mike.li on 2018/5/2.
 */

public class GeTuiIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {
        Logger.i("onReceiveServicePid: "+i);
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        Logger.i("onReceiveClientId: "+s);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("channnelId_BaiDu", s);
        editor.putString("userId_BaiDu", "GETUI");
        editor.apply();
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        Logger.i( "onReceiveMessageData: "+gtTransmitMessage);
        byte[] payload = gtTransmitMessage.getPayload();
        if (payload == null) {
            Logger.i("receiver payload = null");
        } else {
            String data = new String(payload);
            Logger.i("个推 推送数据：" + data);
            PushSDK.getInstance().sendMessage(context,data);
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Logger.i("onReceiveOnlineState: "+b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Logger.i("onReceiveCommandResult: "+gtCmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Logger.i("onNotificationMessageArrived: "+gtNotificationMessage);
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        Logger.i("onNotificationMessageClicked: "+gtNotificationMessage);
    }
}
