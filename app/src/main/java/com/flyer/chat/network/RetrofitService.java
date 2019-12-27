package com.flyer.chat.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.flyer.chat.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by mike.li on 2018/4/17.
 */

public class RetrofitService {
    protected static RetrofitApi retrofitApi;
    public static RetrofitService getInstance() {
        if(retrofitApi ==null){
            retrofitApi = RetrofitClient.getInstance().create(RetrofitApi.class);
        }
        return new RetrofitService();
    }

    //不带参Get
    public void requestGet(String path,CallBack<String> callBack){
        handString(retrofitApi.requestGet(path),callBack);
    }

    //带参Get
    public void requestGet(String path, HashMap<String,Object> map, CallBack<String> callBack){
        handString(retrofitApi.requestGet(path,map),callBack);
    }

    //不带参Post
    public void requestPost(String path,CallBack<String> callBack){
        handString(retrofitApi.requestPost(path),callBack);
    }

    //带参Post
    public void requestPost(String path, @NonNull HashMap<String,Object> map, CallBack<String> callBack){
        JSONObject json = new JSONObject();
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry:entries){
            json.put(entry.getKey(),entry.getValue());
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toJSONString());
        handString(retrofitApi.requestPost(path,requestBody),callBack);
    }

    //表单Post
    public void requestFormPost(String path,@NonNull HashMap<String,Object> map,CallBack<String> callBack){
        handString(retrofitApi.requestFormPost(path,map),callBack);
    }

    //上传文件
    public void uploadFile(String path, File file, CallBack<String> callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        handString(retrofitApi.upload(path,part),callBack);
    }

    //下载文件
    public void downloadFile(final String url,String fileName, final CallBack<File> callBack){
        handFile(retrofitApi.download(url),fileName,callBack);
    }

    //上传图片
    public void uploadImage(String path, File file, CallBack<String> callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        handString(retrofitApi.upload(path,part),callBack);
    }

    //下载无缓存图片
    public void downloadImage(final String url, final CallBack<Bitmap> callBack){
        handBitmap(retrofitApi.download(url),callBack);
    }
    //下载无缓存图片
    public void downloadImage(final String url, Map<String,Object> map, final CallBack<Bitmap> callBack){
        handBitmap(retrofitApi.download(url,map),callBack);
    }

    private void handFile(Observable<ResponseBody> observable, final String fileName, final CallBack<File> callBack) {
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        InputStream is = responseBody.byteStream();
                        File file = new File(FileUtil.getSaveFilePath() + fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        long progress = 0;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                            progress += len;
                            callBack.onProgress(progress / responseBody.contentLength());
                        }
                        fos.close();
                        bis.close();
                        is.close();
                        callBack.onResponse(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callBack.onError(throwable);
                    }
                });
    }

    private void handBitmap(Observable<ResponseBody> observable, final CallBack<Bitmap> callBack) {
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream(), null, options);
                        callBack.onResponse(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callBack.onError(throwable);
                    }
                });
    }

    private void handString(Observable<String> observable, final CallBack<String> callBack){
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String string) {
                        callBack.onResponse(string);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callBack.onError(throwable);
                    }
                });
    }

}
