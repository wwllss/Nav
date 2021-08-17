package zy.example.appa;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import zy.nav.JsonMarshaller;
import zy.nav.annotation.Service;

@Service(JsonMarshaller.class)
public class GsonMarshaller implements JsonMarshaller {

    private static final Gson gson = new Gson();

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
