package zy.nav.doc;

import java.util.List;

public class ArgDoc {

    private String route;

    private List<ArgFieldDoc> argList;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<ArgFieldDoc> getArgList() {
        return argList;
    }

    public void setArgList(List<ArgFieldDoc> argList) {
        this.argList = argList;
    }
}
