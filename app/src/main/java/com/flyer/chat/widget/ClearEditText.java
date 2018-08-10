package com.flyer.chat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/8/8.
 */

public class ClearEditText extends RelativeLayout{
    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initView(Context context){
        View.inflate(context, R.layout.clear_edit_text, this);
    }
}
