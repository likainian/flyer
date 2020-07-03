package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

import com.flyer.chat.R;

/**
 * Created by mike.li on 2020/6/22.
 */
public class TimePickerTwoDialog extends AppCompatDialog {

    public TimePickerTwoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_pick_two);
    }
    public static void showTimeDateAndTime(final Context context) {
    }
}
