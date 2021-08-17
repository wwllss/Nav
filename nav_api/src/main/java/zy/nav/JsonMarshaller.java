package zy.nav;

import java.lang.reflect.Type;

public interface JsonMarshaller {

    String toJson(Object object);

    <T> T fromJson(String json, Type type);

}
