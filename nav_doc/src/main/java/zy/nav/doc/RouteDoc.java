package zy.nav.doc;

import java.util.List;

public class RouteDoc {

    private String route;

    private String className;

    private String comment;

    private List<ArgDoc> argList;

    private List<String> interceptorList;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ArgDoc> getArgList() {
        return argList;
    }

    public void setArgList(List<ArgDoc> argList) {
        this.argList = argList;
    }

    public List<String> getInterceptorList() {
        return interceptorList;
    }

    public void setInterceptorList(List<String> interceptorList) {
        this.interceptorList = interceptorList;
    }
}
