package zy.nav.doc;

public class InterceptorDoc implements Comparable<InterceptorDoc> {

    private String route;

    private String interceptor;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(String interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public int compareTo(InterceptorDoc o) {
        return route.compareTo(o.route);
    }
}
