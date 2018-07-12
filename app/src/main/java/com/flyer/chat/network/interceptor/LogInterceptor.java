package com.flyer.chat.network.interceptor;

import android.support.annotation.NonNull;


import com.flyer.chat.util.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.alibaba.fastjson.util.IOUtils.UTF8;

/**
 * Created by mike.li on 2018/4/17.
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        //url
        HttpUrl url = request.url();
        //request.headers
        Headers headers = request.headers();
        LogUtil.i(url.toString(),"请求header:"+headers.toString());
        //requestBody
        RequestBody requestBody= request.body();
        if(requestBody!=null){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType !=null) {
                charset = contentType.charset(UTF8);
            }
            if(charset!=null){
                String paramsStr = buffer.readString(charset);
                if(paramsStr.isEmpty()||paramsStr.startsWith("{")||paramsStr.startsWith("[")){
                    LogUtil.i(url.toString(),"请求body:"+paramsStr);
                }else {
                    LogUtil.i(url.toString(),"请求body is image or file");
                }
            }
        }else {
            LogUtil.i(url.toString(),"请求body: {}");
        }
        //response
        Response response = chain.proceed(request);
        if(response!=null){
            //response.headers
            Headers responseHeaders = response.headers();
            HttpUrl url2 = response.request().url();
            LogUtil.i(url2.toString(),"返回header:"+responseHeaders.toString());
            //responseBody
            String responseBody = response.peekBody(1024 * 1024).string();
            if(responseBody.startsWith("{")||responseBody.startsWith("[")){
                LogUtil.i(url2.toString(),"返回body:"+responseBody);
            }else {
                LogUtil.i(url2.toString(),"返回body:is bitmap or file");
            }
        }else {
            LogUtil.i(url.toString(),"返回body is empty");
        }
        return response;
    }
}
