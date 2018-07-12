package com.flyer.chat.network.converter;

import android.graphics.Bitmap;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import android.support.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by mike.li on 2018/4/27.
 */

public class ConverterFactory extends Converter.Factory{
    private static Converter.Factory byteConverterFactory;
    private ConverterFactory() {
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(type == Bitmap.class){
            return new BitmapConverter();
        }else if(type == File.class){
            return new FileConverter();
        }else if(type == String.class){
            return new StringConverter();
        }else {
            return new FastJsonConverter(type);
        }
    }

    public synchronized static Converter.Factory create(){
        if(byteConverterFactory==null){
            byteConverterFactory = new ConverterFactory();
        }
        return byteConverterFactory;
    }
}
