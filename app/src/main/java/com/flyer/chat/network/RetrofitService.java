package com.flyer.chat.network;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.flyer.chat.BuildConfig;
import com.flyer.chat.R;
import com.flyer.chat.activity.LoginActivity;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.bean.HttpCode;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.ErrorUtil;
import com.flyer.chat.util.FileUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by mike.li on 2018/4/17.
 */

public class RetrofitService {
    private static RetrofitApi retrofitApi;
    public static RetrofitService getInstance() {
        if(retrofitApi ==null){
            retrofitApi = RetrofitClient.getInstance().create(RetrofitApi.class);
        }
        return new RetrofitService();
    }

    //普通Get
    public void requestGet(String path, CallBack<String> callBack){
        handString(retrofitApi.requestGet(ConstantUtil.getBaseUrl() + path),callBack);
    }

    //普通Post
    public void requestPost(String path, @NonNull RequestBody requestBody, CallBack<String> callBack){
        handString(retrofitApi.requestPost(ConstantUtil.getBaseUrl() + path,requestBody),callBack);
    }

    //普通Post
    public void requestPost(String path, @NonNull Object object, CallBack<String> callBack){
        handString(retrofitApi.requestPost(ConstantUtil.getBaseUrl() + path,object),callBack);
    }

    //用特定Url的普通post
    public void requestPostFullPath(String url, @NonNull RequestBody requestBody, CallBack<String> callBack){
        handString(retrofitApi.requestPost(url,requestBody),callBack);
    }

    //上传图片
    public void uploadImage(String path, MultipartBody.Part part, CallBack<String> callBack){
        handString(retrofitApi.uploadImage(ConstantUtil.getBaseUrl() + path,part),callBack);
    }

    //下载无缓存图片
    public void requestImageNoCache(final String url, final CallBack<Bitmap> callBack){
        retrofitApi.requestImage(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        callBack.onResponse(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(ErrorUtil.getExceptionError(throwable));
                    }
                });
    }

    //下载图片
    public void requestImage(final String url, final CallBack<Bitmap> callBack){
        Bitmap cacheBitmap = FileUtil.getCacheBitmap(url);
        if(cacheBitmap!=null){
            callBack.onResponse(cacheBitmap);
            return;
        }
        retrofitApi.requestImage(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        callBack.onResponse(bitmap);
                        FileUtil.saveCacheBitmap(url,bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(ErrorUtil.getExceptionError(throwable));
                    }
                });
    }

    private void handString(Observable<String> observable, final CallBack<String> callBack){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String string) throws Exception {
                        HttpCode httpCode = JSONObject.parseObject(string, HttpCode.class);
                        if(httpCode!=null&&httpCode.isOk()){
                            callBack.onResponse(string);
                        }else if(httpCode!=null&&httpCode.isNeedLogin()){
                            Intent intent = new Intent(ChatApplication.getInstance(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ChatApplication.getInstance().startActivity(intent);
                        }else if(httpCode!=null&&httpCode.getError()!=null&&httpCode.getError().getMessage()!=null){
                            callBack.onError(httpCode.getError().getMessage());
                        }else {
                            callBack.onError(CommonUtil.getString(R.string.toast_response_data_error));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(BuildConfig.DEBUG)throwable.printStackTrace();
                        callBack.onError(ErrorUtil.getExceptionError(throwable));
                    }
                });
    }

}
