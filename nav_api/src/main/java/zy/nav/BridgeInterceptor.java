package zy.nav;

import android.os.Bundle;

import java.util.Map;

import zy.nav.annotation.FieldType;
import zy.nav.exception.NavException;

class BridgeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws NavException {
        Request request = chain.request();
        Response response = chain.process(request);
        if (!response.success()) {
            return response;
        }
        Bundle bundle = new Bundle(request.params());
        bundle.putString(Nav.RAW_URI, request.url());
        ArgsInjector injector = ArgsInjectorManager
                .getInjector(response.foundClass());
        if (injector != null) {
            Map<String, Integer> paramTypes = injector.getParamTypes();
            if (paramTypes == null || paramTypes.isEmpty()) {
                return response;
            }
            for (Map.Entry<String, Integer> entry : paramTypes.entrySet()) {
                String argName = entry.getKey();
                Integer argType = entry.getValue();
                Object obj = bundle.get(argName);
                String value = String.valueOf(obj);
                if (obj == null
                        || Utils.isEmpty(value)
                        || argType == FieldType.STRING.ordinal()
                        || argType == FieldType.OBJECT.ordinal()) {
                    continue;
                }
                if (argType == FieldType.BOOLEAN.ordinal()) {
                    bundle.putBoolean(argName, Boolean.parseBoolean(value));
                }
                if (argType == FieldType.BYTE.ordinal()) {
                    bundle.putByte(argName, Byte.parseByte(value));
                }
                if (argType == FieldType.SHORT.ordinal()) {
                    bundle.putShort(argName, Short.parseShort(value));
                }
                if (argType == FieldType.INT.ordinal()) {
                    bundle.putInt(argName, Integer.parseInt(value));
                }
                if (argType == FieldType.LONG.ordinal()) {
                    bundle.putLong(argName, Long.parseLong(value));
                }
                if (argType == FieldType.CHAR.ordinal()) {
                    bundle.putChar(argName, value.charAt(0));
                }
                if (argType == FieldType.FLOAT.ordinal()) {
                    bundle.putFloat(argName, Float.parseFloat(value));
                }
                if (argType == FieldType.DOUBLE.ordinal()) {
                    bundle.putDouble(argName, Double.parseDouble(value));
                }
            }
        }
        response.params(bundle);
        return response;
    }

}
