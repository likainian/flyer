package com.flyer.chat.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/9/3.
 */

public class EmoticonView extends FrameLayout{
    public EmoticonView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public EmoticonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        View.inflate(context, R.layout.view_emoticon_layout, this);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }
}
