package com.flyer.chat.util;

import com.mob.ums.User;

import java.util.HashMap;

/**
 * Created by mike.li on 2018/9/17.
 */

public class CacheUtil {
    private static HashMap<String, Object>  map = new HashMap<>();
    public static void saveUser(User user){
        map.put("cache_user",user);
    }
    public static User getUser(){
        if(map.containsKey("cache_user")){
            return (User) map.get("cache_user");
        }else {
            return null;
        }
    }
}
