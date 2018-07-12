package com.flyer.chat.util;

import java.util.Collection;
import java.util.Map;

public final class CheckUtil {

    public static <T> boolean isNotEmpty(T obj) {
        return !isEmpty(obj);
    }

    public static <T> boolean isEmpty(T obj) {
        if (null == obj) return true;
        if (obj instanceof CharSequence) {
            return isEmptyString((CharSequence) obj);
        } else if (obj instanceof Collection) {
            return isEmptyCollection((Collection) obj);
        } else if (obj instanceof Map) {
            return isEmptyMap((Map) obj);
        } else if (obj.getClass().isArray()) {
            return isEmptyArray((Object[]) obj);
        }
        return true;
    }

    private static boolean isEmptyString(CharSequence obj) {
        return null == obj || obj.length() == 0;
    }

    private static boolean isEmptyCollection(Collection obj) {
        return null == obj || obj.isEmpty() || obj.size() == 0;
    }

    private static boolean isEmptyMap(Map obj) {
        return null == obj || obj.isEmpty() || 0 == obj.size();
    }

    private static boolean isEmptyArray(Object[] obj) {
        return 0 == obj.length;
    }

    public static <T> boolean isValidListPosition(Collection<T> collections, int index) {
        return !(CheckUtil.isEmpty(collections) || index < 0 || index >= collections.size());
    }

}
