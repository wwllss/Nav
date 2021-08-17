package zy.nav;

import android.text.TextUtils;
import android.util.LruCache;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ServiceManager {

    private static final LruCache<String, Constructor<?>> CACHE = new LruCache<>(66);

    static <T> T getService(Class<T> serviceClass, String token, Object... params) {
        return newInstance(NavRegistry.getService(key(serviceClass.getName(), token)), params);
    }

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(String className, Object... params) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        List<Class<?>> classList = new ArrayList<>();
        if (params != null && params.length != 0) {
            for (Object object : params) {
                classList.add(object == null ? null : object.getClass());
            }
        }
        Class<?>[] classes = classList.toArray(new Class[0]);
        Constructor<?> constructor = CACHE.get(className + Arrays.toString(classes));
        if (constructor == null) {
            constructor = Utils.getConstructor(className, classes);
            if (constructor == null) {
                return null;
            }
            CACHE.put(className + Arrays.toString(classes), constructor);
        }
        try {
            return (T) constructor.newInstance(params);
        } catch (Exception e) {
            Logger.e("error newInstance ---> " + className);
        }
        return null;
    }

    private static String key(String className, String token) {
        if (TextUtils.isEmpty(token)) {
            return className;
        }
        return className + "-" + token;
    }
}
