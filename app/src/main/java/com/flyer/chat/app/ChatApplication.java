package com.flyer.chat.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.flyer.chat.util.CheckUtil;

import java.util.ArrayList;
import java.util.Iterator;

import cn.bmob.v3.Bmob;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ChatApplication extends Application {
    private static ChatApplication instance;
    public ArrayList<Activity> activities = new ArrayList<>();
    public static synchronized ChatApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Bmob.initialize(this,"0421c676074eef0cf5e75b563ac2c079");
    }

    public void addActivity(Activity activity) {
        if (activity == null)return;
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity == null) return;
        activities.remove(activity);
    }

    public void clearAllActivities() {
        if (CheckUtil.isEmpty(activities)) return;
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity == null) {
                iterator.remove();
                continue;
            }
            iterator.remove();
            if(activity.isFinishing()) return;
            activity.finish();
        }
    }

}
