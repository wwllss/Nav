// Generated code. Do not modify!
package zy.nav.route;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zy.nav.ArgsInjector;
import zy.nav.JsonMarshaller;
import zy.nav.Nav;
import zy.nav.TypeToken;
import zy.nav.util.Person;

public class TestNavFragment1$$ArgsInjector implements ArgsInjector {
    @Override
    public void inject(Object target) {
        TestNavFragment1 host = (TestNavFragment1) target;
        JsonMarshaller marshaller = Nav.getService(JsonMarshaller.class);
        host.aBoolean = host.getArguments().getBoolean("aBoolean", host.aBoolean);
        host.aByte = host.getArguments().getByte("aByte", host.aByte);
        host.aShort = host.getArguments().getShort("aShort", host.aShort);
        host.anInt = host.getArguments().getInt("anInt", host.anInt);
        host.aLong = host.getArguments().getLong("aLong", host.aLong);
        host.aFloat = host.getArguments().getFloat("aFloat", host.aFloat);
        host.aDouble = host.getArguments().getDouble("aDouble", host.aDouble);
        host.aChar = host.getArguments().getChar("aChar", host.aChar);
        host.aString = host.getArguments().getString("aString");
        if (marshaller != null) {
            host.anObject = marshaller.fromJson(host.getArguments().getString("anObject"), new TypeToken<Person>() {
            }.getType());
        } else {
            Log.e("Nav", "must implements zy.nav.ArgsInjector to inject an Object argument");
        }

        if (marshaller != null) {
            host.aMap = marshaller.fromJson(host.getArguments().getString("aMap"), new TypeToken<HashMap<String, List<Person>>>() {
            }.getType());
        } else {
            Log.e("Nav", "must implements zy.nav.ArgsInjector to inject an Object argument");
        }

    }

    @Override
    public Map<String, Integer> getParamTypes() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("aBoolean", 0);
        map.put("aByte", 1);
        map.put("aShort", 2);
        map.put("anInt", 3);
        map.put("aLong", 4);
        map.put("aFloat", 6);
        map.put("aDouble", 7);
        map.put("aChar", 5);
        map.put("aString", 8);
        map.put("anObject", 9);
        map.put("aMap", 9);
        return map;
    }
}
