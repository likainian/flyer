package com.flyer.chat.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;


import com.flyer.chat.util.SharedPreferencesUtil;

import java.util.Locale;

public class BaseContextWrapper extends ContextWrapper {

    public BaseContextWrapper(Context base) {
        super(base);
    }
    public static ContextWrapper wrap(Context context, String language) {
        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale locale;
            if(language.equals(SharedPreferencesUtil.ENGLISH)){
                locale = Locale.ENGLISH;
            }else {
                locale = Locale.CHINA;
            }
            Locale.setDefault(locale);
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        }
        return new BaseContextWrapper(context);
    }
}
