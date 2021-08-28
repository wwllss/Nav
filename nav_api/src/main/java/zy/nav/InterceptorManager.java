package zy.nav;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class InterceptorManager {

    private static final Map<String, List<Interceptor>> MAP = new LinkedHashMap<>();

    static List<Interceptor> getInterceptorList(String route) {
        List<Interceptor> interceptorList = MAP.get(route);
        if (!Utils.isEmpty(interceptorList)) {
            return interceptorList;
        } else {
            interceptorList = new LinkedList<>();
        }
        Map<Integer, String> map = NavRegistry.getInterceptorMap(route);
        if (map == null || map.isEmpty()) {
            return interceptorList;
        }
        List<Map.Entry<Integer, String>> entryList =
                new LinkedList<>(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, String>>() {
            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        for (Map.Entry<Integer, String> entry : entryList) {
            Constructor<Object> constructor = Utils.getConstructor(entry.getValue());
            if (constructor == null) {
                continue;
            }
            try {
                interceptorList.add((Interceptor) constructor.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        MAP.put(route, interceptorList);
        return interceptorList;
    }

}
