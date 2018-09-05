package com.flyer.chat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.flyer.chat.R;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.DeviceUtil;

/**
 * Created by mike.li on 2018/8/8.
 */

public class ClearEditText extends AppCompatEditText{
    private Context context;
    private Drawable mClearDrawable;
    public ClearEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    private void init() {
        mClearDrawable = getResources().getDrawable(R.drawable.clear_edit_text);
        mClearDrawable.setBounds(0, 0, DeviceUtil.dip2px(context,20),DeviceUtil.dip2px(context,20));
        setClearIconVisible(false);
        this.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                setClearIconVisible(s.length() > 0);
            }
        });
    }
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - DeviceUtil.dip2px(context,20))
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        performClick();
        return super.onTouchEvent(event);
    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
