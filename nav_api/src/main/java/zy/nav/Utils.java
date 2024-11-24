package zy.nav;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class Utils {

    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    static boolean equals(String a, String b) {
        return a != null && a.equals(b);
    }

    static void requireNotEmpty(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is null or empty");
        }
    }

    static String getPath(String url) {
        if (isEmpty(url)) {
            return "";
        }
        return Uri.parse(url).getPath();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(String className, Class<?>... paramTypes) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        try {
            Class<T> clazz = (Class<T>) Class.forName(className);
            if (paramTypes == null || paramTypes.length == 0) {
                return clazz.getConstructor();
            }
            return getConstructor(clazz, Arrays.asList(paramTypes));
        } catch (Exception e) {
            Logger.e(className + " getDefaultConstructor error ---> " + e.getMessage());
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(Class<T> clazz, List<Class<?>> paramTypeList) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == paramTypeList.size()) {
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    Class<?> givenParamType = paramTypeList.get(i);
                    match &= isParamEqual(givenParamType, parameterType);
                }
                if (match) {
                    return (Constructor<T>) constructor;
                }
            }
        }
        return null;
    }

    static boolean isParamEqual(Class<?> givenParamType, Class<?> parameterType) {
        if (parameterType == null) {
            return false;
        }
        if (parameterType == Object.class) {
            return true;
        }
        if (givenParamType == null) {
            return !parameterType.isPrimitive();
        }
        if (parameterType.isAssignableFrom(givenParamType)) {
            return true;
        }
        if (givenParamType.isArray() && parameterType.isArray()) {
            return isParamEqual(givenParamType.getComponentType(), parameterType.getComponentType());
        }
        if (!parameterType.isPrimitive() && !givenParamType.isPrimitive()) {
            return false;
        }
        return (parameterType == boolean.class && givenParamType == Boolean.class)
                || (parameterType == char.class && givenParamType == Character.class)
                || (parameterType == byte.class && givenParamType == Byte.class)
                || (parameterType == short.class && givenParamType == Short.class)
                || (parameterType == int.class && givenParamType == Integer.class)
                || (parameterType == long.class && givenParamType == Long.class)
                || (parameterType == float.class && givenParamType == Float.class)
                || (parameterType == double.class && givenParamType == Double.class)
                || (!parameterType.isPrimitive() && isParamEqual(parameterType, givenParamType));
    }

}
