package com.flyer.chat.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.fragment.HomeFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mike.li on 2018/7/9.
 */

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private String SHARED_USER_SESSION = "shared_user_session";
    public static final String LANGUAGE = "language";
    public static final String HOME_MODE = "home_mode";
    public static final String CHINA = "zh_CN";
    public static final String ENGLISH = "en";
    public SharedPreferencesHelper(Application application) {
        sharedPreferences = application.getSharedPreferences(application.getPackageName(), MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesHelper getInstance() {
        if (null == instance) {
            instance = new SharedPreferencesHelper(ChatApplication.getInstance());
        }
        return instance;
    }

    public Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("session",getUdid());
        return map;
    }

    public void putString(String key,String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    public void putBoolean(String key,boolean value) {
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    private void putLong(String key,long value){
        sharedPreferences.edit().putLong(key,value).apply();
    }

    public String getString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    public long getLong(String key, long value) {
        return sharedPreferences.getLong(key, value);
    }
    public String getUdid() {
        String uuid = sharedPreferences.getString(SHARED_USER_SESSION, "");
        if(CheckUtil.isEmpty(uuid)){
            String deviceUuid = DeviceUtil.getDeviceUuid().toString();
            sharedPreferences.edit().putString(SHARED_USER_SESSION,deviceUuid).apply();
            return deviceUuid;
        }else {
            return uuid;
        }
    }
    public String getLanguage(){
        Configuration config = ChatApplication.getInstance().getResources().getConfiguration();
        Locale sysLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        String def;
        if (sysLocale.getLanguage().equals(ENGLISH)) {
            def = ENGLISH;
        } else {
            def = CHINA;
        }
        return getString(SharedPreferencesHelper.LANGUAGE,def);
    }

    public void putLanguage(String language){
        putString(SharedPreferencesHelper.LANGUAGE, language);
        Resources res = ChatApplication.getInstance().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale locale;
        if(language.equals(ENGLISH)){
            locale = Locale.ENGLISH;
        }else {
            locale = Locale.CHINA;
        }
        Locale.setDefault(locale);
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        TimeUtil.switchLocal(locale);
    }
    public void putHomeMode(String mode){
        putString(HOME_MODE,mode);
    }
    public String getHomeMode(){
        return getString(HOME_MODE, HomeFragment.LIST_MODE);
    }
}
