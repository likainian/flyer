package com.flyer.chat.network.converter;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by mike.li on 2018/4/27.
 */

public class FastJsonConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonConverter(Type type) {
        this.type = type;
    }
    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        return JSON.parseObject(value.string(),type);
    }
}
