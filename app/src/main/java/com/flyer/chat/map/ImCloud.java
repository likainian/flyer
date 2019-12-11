package com.flyer.chat.map;

import com.alibaba.fastjson.JSON;
import com.flyer.chat.bean.LinkUser;
import com.flyer.chat.network.UMSCallback;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

import java.util.HashMap;

/**
 * Created by mike.li on 2018/11/29.
 */

public class ImCloud {
    public static void setMapId(final String mapId, final UMSCallback<Void> umsCallback){
        getUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                LinkUser linkUser = HttpParseUtil.parseObject(user.signature.get(), LinkUser.class);
                if(linkUser == null){
                    linkUser = new LinkUser();
                }
                linkUser.setMapId(mapId);
                SharedPreferencesUtil.getInstance().setLinkUser(linkUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("signature", JSON.toJSONString(linkUser));
                UMSSDK.updateUserInfo(map,umsCallback);
            }
        });
    }
    public static void setToken(final String token, final UMSCallback<Void> umsCallback){
        getUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                LinkUser linkUser = HttpParseUtil.parseObject(user.signature.get(), LinkUser.class);
                if(linkUser == null){
                    linkUser = new LinkUser();
                }
                linkUser.setToken(token);
                SharedPreferencesUtil.getInstance().setLinkUser(linkUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("signature", JSON.toJSONString(linkUser));
                UMSSDK.updateUserInfo(map,umsCallback);
            }
        });
    }
    public static void getSignature(final UMSCallback<LinkUser> umsCallback){
        getUser(new UMSCallback<User>(){
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                String signature = user.signature.get();
                LinkUser linkUser = HttpParseUtil.parseObject(signature, LinkUser.class);
                umsCallback.onSuccess(linkUser);
            }
        });
    }

    public static void getUser(UMSCallback<User> umsCallback){
        UMSSDK.getLoginUser(umsCallback);
    }
}
