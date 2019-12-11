package com.flyer.chat.map;

import com.flyer.chat.bean.MapUser;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.TimeUtil;

import java.util.List;

/**
 * Created by mike.li on 2018/11/29.
 */

public class NewCloud {
    public static void getToken(final CallBack<String> callBack){
        MapCloud.getMapUser("1", new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                List<MapUser> users = HttpParseUtil.parseArray(response, "datas", MapUser.class);
                MapUser mapUser = users.get(0);
                Long aLong = TimeUtil.parseToLong(mapUser.getUpdatetime(), TimeUtil.FORMAT_DATE_TIME_SECOND);
                String string = TimeUtil.longToString(aLong, TimeUtil.FORMAT_DATE_TIME_SECOND);
                LogUtil.i("ttt","时间:"+string);
                if(aLong<System.currentTimeMillis()+7200*1000){
                    LogUtil.i("ttt","时间:"+aLong);
                    LogUtil.i("ttt","时间:"+(System.currentTimeMillis()+7200*1000));
                    callBack.onResponse(mapUser.getAddress());
                }else {
                    getServiceToken(callBack);
                }
            }
        });
    }

    private static void getServiceToken(final CallBack<String> callBack){
        RetrofitService.getInstance().requestPost("https://auth.om.qq.com/omoauth2/accesstoken?grant_type=clientcredentials&client_id=a68aa6ae30017235632b558a7a308d7f&client_secret=ff847b753ab3169ff7089dbe4a1446ee1cc4c031", new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                String code = HttpParseUtil.parseString(response, "code");
                if("0".equals(code)){
                    String data = HttpParseUtil.parseString(response, "data");
                    final String token = HttpParseUtil.parseString(data, "access_token");
                    MapCloud.updateMapUser("1", token, new CallBack<String>() {
                        @Override
                        public void onResponse(String response) {
                            callBack.onResponse(token);
                        }
                    });
                }
            }
        });
    }
}
