package com.flyer.chat.network.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by mike.li on 2018/4/27.
 */

public class BitmapConverter implements Converter<ResponseBody, Bitmap> {
    @Override
    public Bitmap convert(@NonNull ResponseBody value) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeStream(value.byteStream(), null, options);
    }
}
