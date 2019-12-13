package com.flyer.chat.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.bean.LinkUser;
import com.flyer.chat.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mike.li on 2018/7/9.
 */

public class SharedPreferencesUtil {
    private static SharedPreferencesUtil instance;
    private SharedPreferences sharedPreferences;
    public static final String LANGUAGE = "language";
    public static final String CHINA = "zh_CN";
    public static final String ENGLISH = "en";
    public SharedPreferencesUtil(Application application) {
        sharedPreferences = application.getSharedPreferences(application.getPackageName(), MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesUtil getInstance() {
        if (null == instance) {
            instance = new SharedPreferencesUtil(ChatApplication.getInstance());
        }
        return instance;
    }

    public void remove(String key){
        sharedPreferences.edit().remove(key).apply();
    }

    public void putString(String key,String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    public String getString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public void putInteger(String key,int value) {
        sharedPreferences.edit().putInt(key,value).apply();
    }

    public int getInteger(String key, int value) {
        return sharedPreferences.getInt(key, value);
    }

    public void putBoolean(String key,boolean value) {
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    private void putLong(String key,long value){
        sharedPreferences.edit().putLong(key,value).apply();
    }

    public long getLong(String key, long value) {
        return sharedPreferences.getLong(key, value);
    }

    private void putFloat(String key,float value){
        sharedPreferences.edit().putFloat(key,value).apply();
    }

    public float getFloat(String key, float value) {
        return sharedPreferences.getFloat(key, value);
    }

    public <T> void putObject(String key,T object) {
        if(object==null){
            remove(key);
        }else {
            sharedPreferences.edit().putString(key, JSONObject.toJSONString(object)).apply();
        }
    }

    public <T> T getObject(String key,Class<T> clazz) {
        String string = sharedPreferences.getString(key, "");
        if(CheckUtil.isEmpty(string)){
            return null;
        }else {
            try {
                return JSONObject.parseObject(string,clazz);
            }catch (Exception e){
                return null;
            }
        }
    }

    public <T> void putObjectList(String key, List<T> list) {
        if (CheckUtil.isNotEmpty(list)) {
            putString(key, JSON.toJSONString(list));
        } else {
            remove(key);
        }
    }

    public <T> List<T> getObjectList(String key, Class<T> clazz) {
        String jsonString = getString(key, "");
        if (CheckUtil.isNotEmpty(jsonString)) {
            return JSON.parseArray(jsonString, clazz);
        }else {
            return new ArrayList<>();
        }
    }

    public void setUserName(String name){
        putString("user_name",name);
        setMobileNoList(name);
    }

    public String getPhoneNum(){
        return getString("phone_num","");
    }

    public void setPhoneNum(String name){
        putString("phone_num",name);
        setMobileNoList(name);
    }

    public String getUserName(){
        return getString("user_name","");
    }

    public void setMobileNoList(String name){
        if(CheckUtil.isEmpty(name))return;
        List<String> userNameList = getMobileNoList();
        if(!userNameList.contains(name)){
            userNameList.add(name);
        }
        putObjectList("user_mobile_list",userNameList);
    }

    public List<String> getMobileNoList(){
        return getObjectList("user_mobile_list",String.class);
    }

    public void setMobileNo(String mobileNo){
        putString("user_mobile",mobileNo);
        setMobileNoList(mobileNo);
    }

    public String getMobileNo(){
        return getString("user_mobile","");
    }

    public String getPassword(){
        return getString("pass_word_"+getUserName(),"");
    }

    public void setPassword(String passWord) {
        putString("pass_word_" + getUserName(), passWord);
    }
    public void setSavePassword(boolean save){
        putBoolean("save_pass_word",save);
    }

    public boolean getSavePassword(){
        return getBoolean("save_pass_word",false);
    }

    public void setKeyboardHeight(int height){
        putInteger("keyboard_height",height);
    }

    public int getKeyboardHeight(){
        return getInteger("keyboard_height",600);
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
        return getString(SharedPreferencesUtil.LANGUAGE,def);
    }

    public void putLanguage(String language){
        putString(SharedPreferencesUtil.LANGUAGE, language);
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

    public void setHomeMode(String mode){
        putString("home_mode",mode);
    }

    public String getHomeMode(){
        return getString("home_mode", HomeFragment.LIST_MODE);
    }

    public void setLinkUser(LinkUser linkUser){
        putObject("link_user",linkUser);
    }

    public LinkUser getLinkUser(){
        return getObject("link_user",LinkUser.class);
    }
}
