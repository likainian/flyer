package com.flyer.chat.network;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by mike.li on 2018/5/2.
 */

public class FileRequestBody {
    public RequestBody build(String path){
        return RequestBody.create(MediaType.parse("text/plain"), new File(path));
    }
    public RequestBody build(File file){
        return RequestBody.create(MediaType.parse("text/plain"), file);
    }
}
