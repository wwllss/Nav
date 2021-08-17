package zy.nav;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class NavRegistry {

    private static final Map<String, String> ACTIVITY = new LinkedHashMap<>();

    private static final Map<String, String> FRAGMENT = new LinkedHashMap<>();

    private static final Map<String, String> SERVICE = new LinkedHashMap<>();

    private static final Map<String, Map<Integer, String>> INTERCEPTOR = new LinkedHashMap<>();

    @SuppressWarnings("unused")
    static void register(String className) {
        if (TextUtils.isEmpty(className)) {
            return;
        }
        try {
            Object obj = Class.forName(className)
                    .getConstructor()
                    .newInstance();
            if (obj instanceof ActivityRegister) {
                register((ActivityRegister) obj);
            } else if (obj instanceof ServiceRegister) {
                register((ServiceRegister) obj);
            } else if (obj instanceof FragmentRegister) {
                register((FragmentRegister) obj);
            } else if (obj instanceof InterceptorRegister) {
                register((InterceptorRegister) obj);
            } else {
                Logger.e(className + " register failed");
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    static void register(ActivityRegister register) {
        if (register != null) {
            register.register(ACTIVITY);
        }
    }

    static void register(FragmentRegister register) {
        if (register != null) {
            register.register(FRAGMENT);
        }
    }

    static void register(ServiceRegister register) {
        if (register != null) {
            register.register(SERVICE);
        }
    }

    static void register(InterceptorRegister register) {
        if (register != null) {
            register.register(INTERCEPTOR);
        }
    }

    static String getActivity(final String key) {
        return ACTIVITY.get(key);
    }

    static void findActivity(Finder<String> finder) {
        for (Map.Entry<String, String> entry : ACTIVITY.entrySet()) {
            finder.find(entry.getKey(), entry.getValue());
        }
    }

    static String getFragment(final String key) {
        return FRAGMENT.get(key);
    }

    static void findFragment(Finder<String> finder) {
        for (Map.Entry<String, String> entry : FRAGMENT.entrySet()) {
            finder.find(entry.getKey(), entry.getValue());
        }
    }

    static String getService(final String key) {
        return SERVICE.get(key);
    }

    static void findService(Finder<String> finder) {
        for (Map.Entry<String, String> entry : SERVICE.entrySet()) {
            finder.find(entry.getKey(), entry.getValue());
        }
    }

    static Map<Integer, String> getInterceptorMap(final String key) {
        return INTERCEPTOR.get(key);
    }

    static void findInterceptorMap(Finder<Map<Integer, String>> finder) {
        for (Map.Entry<String, Map<Integer, String>> entry : INTERCEPTOR.entrySet()) {
            finder.find(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }

    interface Finder<T> {

        void find(String key, T value);

    }

}
