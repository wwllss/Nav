package zy.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

public final class Response {

    private boolean success;

    private String message;

    private Class<?> foundClass;

    private Bundle params;

    private WeakReference<Fragment> fragment;

    public Response() {
        this.params = new Bundle();
    }

    public static Response newResponse() {
        return new Response();
    }

    void target(String className) {
        try {
            target(Class.forName(className));
        } catch (ClassNotFoundException e) {
            success(false);
            Logger.e("ClassNotFoundException --- " + className + ",error message is " + e.getMessage());
        }
    }

    private void target(Class<?> foundClass) {
        this.foundClass = foundClass;
        success(true);
    }

    public Class<?> foundClass() {
        return foundClass;
    }

    public Bundle extras() {
        return new Bundle(params);
    }

    Bundle params() {
        return params;
    }

    void params(Bundle params) {
        this.params = new Bundle(params);
    }

    public boolean success() {
        return success;
    }

    public String message() {
        return message;
    }

    private void success(boolean success) {
        this.success = success;
    }

    public void message(String message) {
        this.message = message;
    }

    public static Response failure(String message) {
        Response response = newResponse();
        response.success(false);
        response.message(message);
        return response;
    }

    Fragment fragment() {
        return this.fragment == null ? null : this.fragment.get();
    }

    void fragment(Fragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }
}
