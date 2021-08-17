package zy.nav;

import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

final class NavDelegate {

    final Request request;

    private final List<Interceptor> interceptorList;

    NavDelegate(Initiator initiator) {
        this.request = Request.newRequest(initiator);
        interceptorList = new ArrayList<>();
    }

    void to(String url, int requestCode) {
        request.url(url);
        request.action(NavAction.ACTIVITY);
        request.requestCode(requestCode);
        Call.newCall(interceptorList, request).call();
    }

    Fragment getFragment(String url) {
        request.url(url);
        request.action(NavAction.FRAGMENT);
        return Call.newCall(interceptorList, request).call().fragment();
    }

    static void inject(Object target) {
        ArgsInjectorManager.inject(target);
    }

    static <T> T getService(Class<T> serviceClass, String token, Object... params) {
        return ServiceManager.getService(serviceClass, token, params);
    }

    static void register(ActivityRegister register) {
        NavRegistry.register(register);
    }

    static void register(FragmentRegister register) {
        NavRegistry.register(register);
    }

    static void register(ServiceRegister register) {
        NavRegistry.register(register);
    }

    static void register(InterceptorRegister register) {
        NavRegistry.register(register);
    }

    void addFlag(int flag) {
        request.addFlag(flag);
    }

    void withOptions(ActivityOptionsCompat options) {
        request.options(options);
    }

    void withInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    void withAll(Bundle bundle) {
        request.params().putAll(bundle);
    }

    void withBoolean(String key, boolean value) {
        request.params().putBoolean(key, value);
    }

    void withByte(String key, byte value) {
        request.params().putByte(key, value);
    }

    void withChar(String key, char value) {
        request.params().putChar(key, value);
    }

    void withShort(String key, short value) {
        request.params().putShort(key, value);
    }

    void withInt(String key, int value) {
        request.params().putInt(key, value);
    }

    void withLong(String key, long value) {
        request.params().putLong(key, value);
    }

    void withFloat(String key, float value) {
        request.params().putFloat(key, value);
    }

    void withDouble(String key, double value) {
        request.params().putDouble(key, value);
    }

    void withString(String key, String value) {
        request.params().putString(key, value);
    }

    void withObject(String key, Object value) {
        JsonMarshaller marshaller = Nav.getService(JsonMarshaller.class);
        if (marshaller != null) {
            request.params().putString(key, marshaller.toJson(value));
        }
    }
}
