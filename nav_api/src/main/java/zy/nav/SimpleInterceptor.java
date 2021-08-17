package zy.nav;

import zy.nav.exception.NavException;

/**
 * created by zhangyuan on 2020/5/18
 */
public abstract class SimpleInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws NavException {
        boolean before = onInterceptBefore(chain.request());
        if (before) {
            throw new NavException("intercept by onInterceptBefore");
        }
        Response response = chain.process(chain.request());
        boolean after = onInterceptAfter(chain.request(), response);
        if (after) {
            throw new NavException("intercept by onInterceptAfter");
        }
        return response;
    }

    /**
     * 寻址前拦截
     *
     * @param request request
     * @return true-拦截，false-继续
     */
    protected boolean onInterceptBefore(Request request) throws NavException {
        return false;
    }

    /**
     * 寻址后&跳转前拦截
     *
     * @param request  request
     * @param response response
     * @return true-拦截，false-继续
     */
    protected boolean onInterceptAfter(Request request, Response response) throws NavException {
        return false;
    }
}
