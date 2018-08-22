package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.DeviceUtil;

/**
 * Created by mike.li on 2018/8/22.
 */

public class NoticeDialog extends AlertDialog {
    private Context context;
    private String title;
    private String message;
    private OnPositiveClickListener onPositiveClickListener;
    private OnNegativeClickListener onNegativeClickListener;
    public interface OnPositiveClickListener{
        void onPositiveClick();
    }
    public interface OnNegativeClickListener{
        void onNegativeClick();
    }
    public NoticeDialog(@NonNull Context context) {
        super(context, R.style.SelectDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
        setCanceledOnTouchOutside(false);
        TextView noticeTitle = findViewById(R.id.notice_title);
        if (noticeTitle != null) {
            if (CheckUtil.isEmpty(title)) {
                noticeTitle.setVisibility(View.GONE);
            } else {
                noticeTitle.setVisibility(View.VISIBLE);
                noticeTitle.setText(title);
            }
        }
        TextView noticeMessage = findViewById(R.id.notice_message);
        if (noticeMessage != null) {
            noticeMessage.setText(message);
        }
        TextView negativeButton = findViewById(R.id.negative_button);
        if (negativeButton != null) {
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(onNegativeClickListener!=null)onNegativeClickListener.onNegativeClick();
                }
            });
        }
        TextView positiveButton = findViewById(R.id.positive_button);
        if (positiveButton != null) {
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(onPositiveClickListener!=null)onPositiveClickListener.onPositiveClick();
                }
            });
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getWindow().getDecorView().setPadding(DeviceUtil.dip2px(context, 20), 0,
                    DeviceUtil.dip2px(context, 20), 0);
            getWindow().setAttributes(layoutParams);
        }
    }

    public NoticeDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public NoticeDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public NoticeDialog setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
        return this;
    }

    public NoticeDialog setOnNegativeClickListener(OnNegativeClickListener onNegativeClickListener) {
        this.onNegativeClickListener = onNegativeClickListener;
        return this;
    }
}
