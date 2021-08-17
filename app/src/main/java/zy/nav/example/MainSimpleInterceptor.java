package zy.nav.example;

import zy.nav.Request;
import zy.nav.SimpleInterceptor;
import zy.nav.annotation.Intercept;
import zy.nav.exception.NavException;

/**
 * created by zhangyuan on 2020/5/18
 */
@Intercept
public class MainSimpleInterceptor extends SimpleInterceptor {

    @Override
    protected boolean onInterceptBefore(Request request) throws NavException {
        return super.onInterceptBefore(request);
    }
}
