package zy.nav.doc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NavDoc {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings("all")
    public static void doc(Object object, String fileName) {
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(gson.toJson(sort(object)));
            fw.flush();
            fw.close();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Object sort(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(object);
            sortIfList(o);
        }
        sortIfList(object);
        return object;
    }

    @SuppressWarnings("all")
    private static void sortIfList(Object o) {
        if (!(o instanceof List)) {
            return;
        }
        Collections.sort((List<Comparable>) o);
    }

    @SuppressWarnings("all")
    public static void merge(String srcDir, String dstDir, List<String> serviceFilterList) {
        try {
            File file = new File(srcDir);
            mergeRoute(file, dstDir);
            mergeService(file, dstDir, serviceFilterList);
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File child : files) {
                child.delete();
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void mergeRoute(File srcDir, String dstDir) {
        Map<String, RouteDoc> activityMap = new LinkedHashMap<>();
        Map<String, RouteDoc> fragmentMap = new LinkedHashMap<>();
        //activity
        List<RouteDoc> activityList = fromJson(srcDir.listFiles((dir, name) -> name.startsWith("activities")),
                new TypeToken<List<RouteDoc>>() {
                }.getType());
        if (!isEmpty(activityList)) {
            for (RouteDoc route : activityList) {
                activityMap.put(route.getRoute(), route);
            }
        }
        //fragments
        List<RouteDoc> fragmentList = fromJson(srcDir.listFiles((dir, name) -> name.startsWith("fragments")),
                new TypeToken<List<RouteDoc>>() {
                }.getType());
        if (!isEmpty(fragmentList)) {
            for (RouteDoc route : fragmentList) {
                fragmentMap.put(route.getRoute(), route);
            }
        }
        //arg
        List<ArgDoc> argList = fromJson(srcDir.listFiles((dir, name) -> name.startsWith("arg")),
                new TypeToken<List<ArgDoc>>() {
                }.getType());
        if (!isEmpty(argList)) {
            for (ArgDoc arg : argList) {
                String route = arg.getRoute();
                fillArg(activityMap.get(route), arg);
                fillArg(fragmentMap.get(route), arg);
            }
        }
        //interceptor
        List<InterceptorDoc> interceptorList = fromJson(srcDir.listFiles((dir, name) -> name.startsWith("interceptor")),
                new TypeToken<List<InterceptorDoc>>() {
                }.getType());
        List<String> globeInterceptor = new LinkedList<>();
        if (!isEmpty(interceptorList)) {
            for (InterceptorDoc interceptor : interceptorList) {
                String route = interceptor.getRoute();
                if (route.isEmpty()) {
                    globeInterceptor.add(interceptor.getInterceptor());
                    continue;
                }
                if (fragmentMap.containsKey(route)) {
                    fillInterceptor(fragmentMap, route, interceptor);
                } else {
                    fillInterceptor(activityMap, route, interceptor);
                }
            }
        }
        if (!globeInterceptor.isEmpty()) {
            doc(globeInterceptor, dstDir + "/interceptor.json");
        }
        writeRoute(activityMap, dstDir + "/activity.json");
        writeRoute(fragmentMap, dstDir + "/fragment.json");
    }

    private static void writeRoute(Map<String, RouteDoc> map, String fileName) {
        List<RouteDoc> routeList = new LinkedList<>();
        if (!map.isEmpty()) {
            for (Map.Entry<String, RouteDoc> entry : map.entrySet()) {
                routeList.add(entry.getValue());
            }
        }
        doc(routeList, fileName);
    }

    private static void fillArg(RouteDoc route, ArgDoc arg) {
        if (route == null || arg == null || isEmpty(arg.getArgList())) {
            return;
        }
        List<ArgFieldDoc> temp = route.getArgList();
        if (temp == null) {
            temp = new LinkedList<>();
            route.setArgList(temp);
        }
        arg.setRoute(null);
        temp.addAll(arg.getArgList());
    }

    private static void fillInterceptor(Map<String, RouteDoc> map, String route, InterceptorDoc interceptor) {
        if (interceptor == null) {
            return;
        }
        RouteDoc doc = map.get(route);
        if (doc == null) {
            doc = new RouteDoc();
            doc.setRoute(route);
            map.put(route, doc);
        }
        List<String> temp = doc.getInterceptorList();
        if (temp == null) {
            temp = new LinkedList<>();
            doc.setInterceptorList(temp);
        }
        interceptor.setRoute(null);
        temp.add(interceptor.getInterceptor());
    }

    private static void mergeService(File srcDir, String dstDir, List<String> serviceFilterList) {
        List<ServiceDoc> serviceList = fromJson(srcDir.listFiles((dir, name) -> name.startsWith("service")),
                new TypeToken<List<ServiceDoc>>() {
                }.getType());
        if (isEmpty(serviceList)) {
            return;
        }
        Map<String, ServiceDoc> allMap = new LinkedHashMap<>();
        List<ServiceDoc> mergeList = new LinkedList<>();
        for (ServiceDoc doc : serviceList) {
            String name = doc.getService();
            ServiceDoc service = allMap.get(name);
            if (service == null) {
                allMap.put(name, doc);
                mergeList.add(doc);
            } else {
                service.getImplList().addAll(doc.getImplList());
            }
        }
        doc(mergeList, dstDir + "/service.json");
        if (isEmpty(serviceFilterList)) {
            return;
        }
        Map<String, ServiceDoc> filterMap = new LinkedHashMap<>();
        for (Map.Entry<String, ServiceDoc> entry : allMap.entrySet()) {
            String key = entry.getKey();
            ServiceDoc value = entry.getValue();
            for (String filter : serviceFilterList) {
                if (!filter.equals(key)) {
                    continue;
                }
                filterMap.put(filter, value);
            }
        }
        if (filterMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ServiceDoc> entry : filterMap.entrySet()) {
            doc(entry.getValue(), dstDir + "/" + entry.getKey() + ".json");
        }
    }

    static <T> List<T> fromJson(File[] files, Type type) {
        if (isEmpty(files)) {
            return null;
        }
        List<T> list = new LinkedList<>();
        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                List<T> fromJson = gson.fromJson(fr, type);
                if (isEmpty(fromJson)) {
                    continue;
                }
                list.addAll(fromJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
