package com.flyer.chat.network;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by mike.li on 2018/4/17.
 * 申请有关接口
 */

interface RetrofitApi {

    //不带参Get
    @GET
    Observable<String> requestGet(@Url String url);

    //带参Get
    @GET
    Observable<String> requestGet(@Url String url, @QueryMap HashMap<String,Object> map);

    //不带参Post
    @POST
    Observable<String> requestPost(@Url String url);

    //带参Post
    @POST
    Observable<String> requestPost(@Url String url, @Body RequestBody requestBody);

    //表单Post
    @FormUrlEncoded
    @POST
    Observable<String> requestFormPost(@Url String url,@FieldMap HashMap<String,Object> map);

    //上传
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part part);

    //下载
    @GET
    Observable<ResponseBody> download(@Url String url);

    //下载
    @GET
    Observable<ResponseBody> download(@Url String url,@QueryMap Map<String,Object> map);

}
