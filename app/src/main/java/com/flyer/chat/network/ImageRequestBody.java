package com.flyer.chat.network;

import com.flyer.chat.util.BitmapUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by mike.li on 2018/5/2.
 */

public class ImageRequestBody {
    public MultipartBody.Part build(String imagePath){
        File imgUrl = BitmapUtil.getCompressImg(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), imgUrl);
        return MultipartBody.Part.createFormData("file", imgUrl.getName(), requestBody);
    }
}
