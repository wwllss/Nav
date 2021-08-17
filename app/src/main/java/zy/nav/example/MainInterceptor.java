package zy.nav.example;

import zy.nav.Interceptor;
import zy.nav.Response;
import zy.nav.annotation.Intercept;
import zy.nav.exception.NavException;

@Intercept(route = "/app/k/main")
public class MainInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws NavException {
        return chain.process(chain.request());
    }
}
