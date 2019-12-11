package com.flyer.chat.push;

import android.content.Context;

import com.flyer.chat.util.LogUtil;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

/**
 * Created by mike.li on 2018/9/7.
 */

public class PushReceiver implements MobPushReceiver {
    private PushReceiver(){}
    private static PushReceiver pushReceiver;
    public static PushReceiver getInstance(){
        if(pushReceiver == null){
            pushReceiver = new PushReceiver();
        }
        return pushReceiver;
    }

    @Override
    public void onCustomMessageReceive(Context context, MobPushCustomMessage mobPushCustomMessage) {
        LogUtil.i("ttt","onCustomMessageReceive"+mobPushCustomMessage.toString());
    }

    @Override
    public void onNotifyMessageReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
        LogUtil.i("ttt","onNotifyMessageReceive"+mobPushNotifyMessage.toString());
    }

    @Override
    public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
        LogUtil.i("ttt","onNotifyMessageOpenedReceive"+mobPushNotifyMessage.toString());
    }

    @Override
    public void onTagsCallback(Context context, String[] strings, int i, int i1) {
        LogUtil.i("ttt","onTagsCallback");
    }

    @Override
    public void onAliasCallback(Context context, String s, int i, int i1) {
        LogUtil.i("ttt","onAliasCallback");
    }
}
