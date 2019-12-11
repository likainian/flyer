package com.flyer.chat.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.flyer.chat.app.ChatApplication;

/**
 * Created by mike.li on 2018/5/4.
 */

public class KeyBoardUtil {
    public interface KeyBoardStatusListener{
        void onKeyBoardStateChanged(boolean isShow, final int height);
    }
    public static void register(final Activity activity, final KeyBoardStatusListener keyBoardStatusListener){
        final View rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //视图变化
                int heightDiff = rootView.getRootView().getHeight() - r.bottom;
                if (heightDiff > 100) {
                    SharedPreferencesUtil.getInstance().setKeyboardHeight(heightDiff);
                    keyBoardStatusListener.onKeyBoardStateChanged(true,heightDiff);
                } else {
                    keyBoardStatusListener.onKeyBoardStateChanged(false,heightDiff);
                }
            }
        });
    }
    public static void showKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) ChatApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }
    public static void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) ChatApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 强制隐藏软键盘
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
