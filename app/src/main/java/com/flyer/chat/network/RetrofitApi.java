package com.flyer.chat.network;

import android.graphics.Bitmap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by mike.li on 2018/4/17.
 * 申请有关接口
 */

interface RetrofitApi {

    //普通Get
    @GET
    Observable<String> requestGet(@Url String url);

    //普通Post
    @POST
    Observable<String> requestPost(@Url String url, @Body RequestBody body);

    //没有body的post
    @POST
    Observable<String> requestPost(@Url String url);

    //上传图片
    @Multipart
    @POST
    Observable<String> uploadImage(@Url String url, @Part MultipartBody.Part part);

    //下载图片
    @GET
    Observable<Bitmap> requestImage(@Url String url);

}
