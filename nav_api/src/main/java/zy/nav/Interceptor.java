package zy.nav;

import zy.nav.exception.NavException;

public interface Interceptor {

    Response intercept(Chain chain) throws NavException;

    interface Chain {

        Request request();

        Response process(Request request) throws NavException;

    }

}
