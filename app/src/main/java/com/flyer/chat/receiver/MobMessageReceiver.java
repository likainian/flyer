package com.flyer.chat.receiver;

import com.flyer.chat.util.LogUtil;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.model.IMMessage;

import java.util.List;

/**
 * Created by mike.li on 2018/9/11.
 */

public class MobMessageReceiver implements MobIMMessageReceiver{
    private static MobMessageReceiver mobMessageReceiver;
    public static MobMessageReceiver getInstance(){
        if(mobMessageReceiver==null){
            mobMessageReceiver = new MobMessageReceiver();
        }
        return mobMessageReceiver;
    }
    @Override
    public void onMessageReceived(List<IMMessage> list) {
        LogUtil.i("ttt","onMessageReceived");
    }

    @Override
    public void onMsgWithDraw(String s, String s1) {

    }
}
