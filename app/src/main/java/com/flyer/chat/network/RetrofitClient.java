package com.flyer.chat.network;

import com.flyer.chat.network.interceptor.HeaderInterceptor;
import com.flyer.chat.network.interceptor.LogInterceptor;
import com.flyer.chat.util.ConstantUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by mike.li on 2018/4/17.
 */

class RetrofitClient {
    private static Retrofit retrofit;
    public synchronized static Retrofit getInstance(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantUtil.getBaseUrl())
                    .client( new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(new HeaderInterceptor())
                            .addInterceptor(new LogInterceptor())
                            .build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
