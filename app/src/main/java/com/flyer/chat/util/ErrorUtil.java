package com.flyer.chat.util;

import com.flyer.chat.R;

import java.net.SocketTimeoutException;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ErrorUtil {
    public static String getExceptionError(Throwable throwable) {
        if(throwable instanceof SocketTimeoutException){
            return "请求超时";
        }
        return CommonUtil.getString(R.string.toast_response_data_error);
    }
}
