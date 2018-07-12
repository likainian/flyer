package com.flyer.chat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mike.li on 2018/5/29.
 */

public class MD5Util {
    public static String toMD5(String src){
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(src.getBytes("UTF-8"));
            byte[] digest = md5.digest(src.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
