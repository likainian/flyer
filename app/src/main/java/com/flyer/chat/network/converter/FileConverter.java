package com.flyer.chat.network.converter;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by mike.li on 2018/4/27.
 */

public class FileConverter implements Converter<ResponseBody, File> {
    @Override
    public File convert(@NonNull ResponseBody value) throws IOException {
        return null;
    }
}
