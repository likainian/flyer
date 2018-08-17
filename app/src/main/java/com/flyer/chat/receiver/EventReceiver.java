package com.flyer.chat.receiver;

import com.flyer.chat.util.LogUtil;

import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;

/**
 * Created by mike.li on 2018/8/14.
 */

public class EventReceiver {
    public void onEvent(Object event){
        //do your own business
        if(event instanceof MessageEvent){
            LogUtil.i("ttt","收到消息"+((MessageEvent) event).getMessage());
        }else if(event instanceof NotificationClickEvent){

        }else if(event instanceof OfflineMessageEvent){

        }else if(event instanceof LoginStateChangeEvent){

        }else if(event instanceof MessageRetractEvent){

        }
        LogUtil.i("ttt","收到消息"+event);
    }
}
