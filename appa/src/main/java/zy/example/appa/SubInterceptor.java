package zy.example.appa;

import zy.nav.Interceptor;
import zy.nav.Response;
import zy.nav.annotation.Intercept;
import zy.nav.exception.NavException;

@Intercept(priority = 10086)
public class SubInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws NavException {
        return chain.process(chain.request());
    }
}
