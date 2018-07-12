package com.flyer.chat.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.flyer.chat.app.ChatApplication;

/**
 * Created by mike.li on 2018/5/15.
 */

public class CommonUtil {
    public static String getString(@StringRes int id) {
        return ChatApplication.getInstance().getString(id);
    }

    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(ChatApplication.getInstance(),colorId);
    }
    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(ChatApplication.getInstance(),drawableId);
    }
}
