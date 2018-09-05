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

public class MultimediaView extends FrameLayout {
    public MultimediaView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MultimediaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        View.inflate(context, R.layout.multimedia_view, this);

    }
}
