package zy.nav;

import android.util.LruCache;

import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

final class ArgsInjectorManager {

    private static final LruCache<Class<?>, Constructor<?>> CACHE = new LruCache<>(66);

    static void inject(Object target) {
        ArgsInjector injector = getInjector(target.getClass());
        if (injector == null) {
            return;
        }
        injector.inject(target);
    }

    @Nullable
    static ArgsInjector getInjector(Class<?> targetClass) {
        Constructor<?> constructor = findConstructorForClass(targetClass);
        if (constructor == null) {
            return null;
        }
        try {
            return (ArgsInjector) constructor.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to create ArgInjector instance for " + targetClass, e);
        }
    }

    private static Constructor<?> findConstructorForClass(Class<?> targetClass) {
        Constructor<?> constructor = CACHE.get(targetClass);
        if (constructor != null) {
            return constructor;
        }
        String className = targetClass.getName();
        if (className.startsWith("android.")
                || className.startsWith("java.")
                || className.startsWith("javax.")) {
            return null;
        }
        try {
            Class<?> injectClass = Class.forName(className + "$$ArgsInjector", true,
                    targetClass.getClassLoader());
            constructor = injectClass.getConstructor();
        } catch (ClassNotFoundException e) {
            constructor = findConstructorForClass(targetClass.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + targetClass, e);
        }
        if (constructor != null) {
            CACHE.put(targetClass, constructor);
        }
        return constructor;
    }
}
