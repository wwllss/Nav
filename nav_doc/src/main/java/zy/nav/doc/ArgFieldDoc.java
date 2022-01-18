package zy.nav.doc;

public class ArgFieldDoc implements Comparable<ArgFieldDoc> {

    private String paramName;

    private String fieldName;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public int compareTo(ArgFieldDoc o) {
        return paramName.compareTo(o.paramName);
    }
}
