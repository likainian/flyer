package com.flyer.chat.util;

import android.app.Service;
import android.os.Vibrator;

import com.flyer.chat.app.ChatApplication;

/**
 * Created by mike.li on 2019/11/8.
 */
public class VibratorUtil {
    public static void start(){
        Vibrator vib = (Vibrator) ChatApplication.getInstance().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(70);
    }
}
