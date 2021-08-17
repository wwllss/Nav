package zy.nav;

import java.util.List;

import zy.nav.exception.NavException;

final class InterceptorChain implements Interceptor.Chain {

    private final List<Interceptor> interceptorList;

    private final Request request;

    private final int index;

    InterceptorChain(List<Interceptor> interceptorList, Request request, int index) {
        this.interceptorList = interceptorList;
        this.request = request;
        this.index = index;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response process(Request request) throws NavException {
        if (index >= interceptorList.size()) {
            throw new AssertionError("index out");
        }
        InterceptorChain next = new InterceptorChain(interceptorList, request, index + 1);
        Interceptor interceptor = interceptorList.get(index);
        return interceptor.intercept(next);
    }
}
