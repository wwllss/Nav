package zy.nav;

import java.util.ArrayList;
import java.util.List;

import zy.nav.exception.RedirectException;

final class Call {

    private final Request request;

    private final List<Interceptor> interceptorList;

    private final RetryAndInitiateInterceptor retryAndInitiateInterceptor;

    private Call(List<Interceptor> interceptorList, Request request) {
        this.interceptorList = interceptorList;
        this.request = request;
        this.retryAndInitiateInterceptor = new RetryAndInitiateInterceptor();
    }

    static Call newCall(List<Interceptor> interceptorList, Request request) {
        return new Call(interceptorList, request);
    }

    final Response call() {
        List<Interceptor> list = new ArrayList<>();
        list.add(retryAndInitiateInterceptor);
        if (!Utils.isEmpty(interceptorList)) {
            list.addAll(interceptorList);
        }
        List<Interceptor> globalInterceptor = InterceptorManager.getInterceptorList("");
        if (!Utils.isEmpty(globalInterceptor)) {
            list.addAll(globalInterceptor);
        }
        //real global interceptor
        String path = Utils.getPath(request.url());
        if (!Utils.isEmpty(path)) {
            globalInterceptor = InterceptorManager.getInterceptorList(path);
            if (!Utils.isEmpty(globalInterceptor)) {
                list.addAll(globalInterceptor);
            }
        }
        list.add(new BridgeInterceptor());
        list.add(new SystemFindInterceptor());
        list.add(new RouteFindInterceptor());
        Interceptor.Chain chain = new InterceptorChain(list, request, 0);
        try {
            return chain.process(request);
        } catch (Exception e) {
            if (e instanceof RedirectException) {
                return call();
            }
            Logger.e(e.getMessage());
            return Response.failure(e.getMessage());
        }
    }

}
