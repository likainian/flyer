package com.flyer.chat.activity.account;

import com.flyer.chat.push.PushReceiver;
import com.flyer.chat.util.LogUtil;
import com.mob.MobSDK;
import com.mob.pushsdk.MobPush;
import com.mob.ums.User;

/**
 * Created by mike.li on 2018/9/11.
 */

public class AccountManager {
    public static void login(User user){
        LogUtil.i("ttt","登陆成功："+user.toString());
        MobSDK.setUser(user.id.get(), user.nickname.get(),user.avatar.get()[0], null);
        MobPush.addPushReceiver(PushReceiver.getInstance());
    }
    public static void logout(){
        MobPush.removePushReceiver(PushReceiver.getInstance());

    }
}
