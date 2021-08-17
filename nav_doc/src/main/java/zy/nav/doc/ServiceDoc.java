package zy.nav.doc;

import java.util.List;

public class ServiceDoc {

    private String service;

    private String comment;

    private List<ServiceImplDoc> implList;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ServiceImplDoc> getImplList() {
        return implList;
    }

    public void setImplList(List<ServiceImplDoc> implList) {
        this.implList = implList;
    }
}
