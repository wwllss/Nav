package zy.nav;

import android.content.Context;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;

import zy.nav.exception.NavException;
import zy.nav.exception.RedirectException;

public final class Request {

    private final Initiator initiator;

    private NavAction action;

    private String url;

    private int requestCode;

    private final Bundle params;

    private int flags;

    private ActivityOptionsCompat options;

    private Request(Initiator initiator) {
        this.initiator = initiator;
        this.params = new Bundle();
    }

    static Request newRequest(Initiator initiator) {
        return new Request(initiator);
    }

    Initiator initiator() {
        return initiator;
    }

    public Context context() {
        return initiator.context();
    }

    void action(NavAction action) {
        this.action = action;
    }

    public NavAction action() {
        return action;
    }

    void url(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    public Bundle params() {
        return params;
    }

    public void options(ActivityOptionsCompat options) {
        this.options = options;
    }

    public ActivityOptionsCompat options() {
        return options;
    }

    public void requestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int requestCode() {
        return requestCode;
    }

    public void addFlag(int flag) {
        this.flags |= flag;
    }

    public int flags() {
        return this.flags;
    }

    public void intercept(String msg) throws NavException {
        throw new NavException(msg);
    }

    public void redirect(String url) throws RedirectException {
        url(url);
        throw new RedirectException("Redirect -> " + url);
    }
}
