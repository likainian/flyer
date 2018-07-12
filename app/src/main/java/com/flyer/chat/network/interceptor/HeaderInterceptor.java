package com.flyer.chat.network.interceptor;

import android.support.annotation.NonNull;


import com.flyer.chat.util.SharedPreferencesHelper;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mike.li on 2018/4/17.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //加入请求头
        Request.Builder builder = chain.request().newBuilder();
        builder.headers(Headers.of(SharedPreferencesHelper.getInstance().getHeaders()));
        Request request = builder.build();
        //response的header更新
        Response response = chain.proceed(request);
        if(response!=null){
            Headers headers = response.headers();
            String newSession = headers.get("session");
            if(newSession!=null&&!newSession.equals(SharedPreferencesHelper.getInstance().getUdid())){
//                SharedPreferencesHelper.getInstance().setUserSession(newSession);
            }
        }
        return response;
    }
}
