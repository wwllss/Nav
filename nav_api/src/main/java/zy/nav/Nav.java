package zy.nav;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

@UiThread
public final class Nav {

    public static final String RAW_URI = "__Nav__Raw__Uri__";

    static final int NO_REQUEST_CODE = -1;

    final NavDelegate delegate;

    private Nav(Initiator initiator) {
        this.delegate = new NavDelegate(initiator);
    }

    public static Nav from(Context context) {
        return from(Initiator.Factory.from(context));
    }

    public static Nav from(Activity activity) {
        return from(Initiator.Factory.from(activity));
    }

    public static Nav from(Fragment fragment) {
        return from(Initiator.Factory.from(fragment));
    }

    private static Nav from(Initiator initiator) {
        return new Nav(initiator);
    }

    public static void register(ActivityRegister register) {
        NavDelegate.register(register);
    }

    public static void register(FragmentRegister register) {
        NavDelegate.register(register);
    }

    public static void register(ServiceRegister register) {
        NavDelegate.register(register);
    }

    public static void register(InterceptorRegister register) {
        NavDelegate.register(register);
    }

    public static void inject(Fragment fragment) {
        NavDelegate.inject(fragment);
    }

    public static void inject(Activity activity) {
        NavDelegate.inject(activity);
    }

    @Nullable
    public static <T> T getService(@NonNull Class<T> serviceClass) {
        return getService(serviceClass, "");
    }

    @Nullable
    public static <T> T getService(@NonNull Class<T> serviceClass, String token, Object... params) {
        return NavDelegate.getService(serviceClass, token, params);
    }

    public static <T> List<T> findServices(Class<T> serviceClass, Object... params) {
        return NavDelegate.findServices(serviceClass, params);
    }

    public void to(String url) {
        delegate.to(url, NO_REQUEST_CODE);
    }

    public void to(String url, @IntRange(from = 1) int requestCode) {
        delegate.to(url, requestCode);
    }

    @Nullable
    public Fragment getFragment(String url) {
        return delegate.getFragment(url);
    }

    public Nav addFlag(int flag) {
        delegate.addFlag(flag);
        return this;
    }

    public Nav withOptions(ActivityOptionsCompat options) {
        delegate.withOptions(options);
        return this;
    }

    public Nav withInterceptor(@NonNull Interceptor interceptor) {
        delegate.withInterceptor(interceptor);
        return this;
    }

    public Nav withAll(@NonNull Bundle bundle) {
        delegate.withAll(bundle);
        return this;
    }

    public Nav withBoolean(String key, boolean bundle) {
        delegate.withBoolean(key, bundle);
        return this;
    }

    public Nav withByte(String key, byte value) {
        delegate.withByte(key, value);
        return this;
    }

    public Nav withChar(String key, char value) {
        delegate.withChar(key, value);
        return this;
    }

    public Nav withShort(String key, short value) {
        delegate.withShort(key, value);
        return this;
    }

    public Nav withInt(String key, int value) {
        delegate.withInt(key, value);
        return this;
    }

    public Nav withLong(String key, long value) {
        delegate.withLong(key, value);
        return this;
    }

    public Nav withFloat(String key, float value) {
        delegate.withFloat(key, value);
        return this;
    }

    public Nav withDouble(String key, double value) {
        delegate.withDouble(key, value);
        return this;
    }

    public Nav withString(String key, String value) {
        delegate.withString(key, value);
        return this;
    }

    public Nav withObject(String key, Object value) {
        delegate.withObject(key, value);
        return this;
    }

}
