package zy.nav;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<T> {

    private Type type;

    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass != null) {
            type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        }
    }

    public final Type getType() {
        return this.type;
    }
}
