package zy.nav;

import android.text.TextUtils;

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

    static Map<String, String> findActivities(Finder finder) {
        return find(ACTIVITY, finder);
    }

    static String getFragment(final String key) {
        return FRAGMENT.get(key);
    }

    static Map<String, String> findFragments(Finder finder) {
        return find(FRAGMENT, finder);
    }

    static String getService(final String key) {
        return SERVICE.get(key);
    }

    static Map<String, String> findServices(Finder finder) {
        return find(SERVICE, finder);
    }

    static Map<Integer, String> getInterceptor(final String key) {
        Map<Integer, String> map = INTERCEPTOR.get(key);
        return map == null ? null : new LinkedHashMap<>(map);
    }

    static Map<String, Map<Integer, String>> findInterceptors(Finder finder) {
        return find(INTERCEPTOR, finder);
    }

    @SuppressWarnings("all")
    private static <T> Map<String, T> find(Map<String, T> source, Finder finder) {
        if (Utils.isEmpty(source)) {
            return new LinkedHashMap<>();
        }
        Map<String, T> map = new LinkedHashMap<>();
        for (Map.Entry<String, T> entry : source.entrySet()) {
            String key = entry.getKey();
            if (!finder.find(key)) {
                continue;
            }
            T value = entry.getValue();
            if (value instanceof Map) {
                value = (T) new LinkedHashMap((Map) value);
            }
            map.put(key, value);
        }
        return map;
    }

    interface Finder {

        boolean find(String key);

    }

}
