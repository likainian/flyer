package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/7/9.
 */

public class LoadingDialog extends AlertDialog {
    private TextView tvMessage;

    public LoadingDialog(Context context) {
        super(context,R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_layout);
        tvMessage = findViewById(R.id.tv_message);
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }
}
