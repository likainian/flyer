package com.flyer.chat.util;

import com.flyer.chat.R;

import retrofit2.HttpException;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ErrorUtil {
    public static String getExceptionError(Throwable throwable) {
        if(throwable instanceof HttpException){
            return CommonUtil.getString(R.string.toast_response_data_error);
        }
        return CommonUtil.getString(R.string.toast_response_data_error);
    }
}
