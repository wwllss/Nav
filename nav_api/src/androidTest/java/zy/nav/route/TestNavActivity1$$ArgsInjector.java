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

public class TestNavActivity1$$ArgsInjector implements ArgsInjector {
    @Override
    public void inject(Object target) {
        TestNavActivity1 host = (TestNavActivity1) target;
        JsonMarshaller marshaller = Nav.getService(JsonMarshaller.class);
        host.aBoolean = host.getIntent().getBooleanExtra("aBoolean", host.aBoolean);
        host.aByte = host.getIntent().getByteExtra("aByte", host.aByte);
        host.aShort = host.getIntent().getShortExtra("aShort", host.aShort);
        host.anInt = host.getIntent().getIntExtra("anInt", host.anInt);
        host.aLong = host.getIntent().getLongExtra("aLong", host.aLong);
        host.aFloat = host.getIntent().getFloatExtra("aFloat", host.aFloat);
        host.aDouble = host.getIntent().getDoubleExtra("aDouble", host.aDouble);
        host.aChar = host.getIntent().getCharExtra("aChar", host.aChar);
        host.aString = host.getIntent().getStringExtra("aString");
        if (marshaller != null) {
            host.anObject = marshaller.fromJson(host.getIntent().getStringExtra("anObject"), new TypeToken<Person>() {
            }.getType());
        } else {
            Log.e("Nav", "must implements zy.nav.ArgsInjector to inject an Object argument");
        }

        if (marshaller != null) {
            host.aMap = marshaller.fromJson(host.getIntent().getStringExtra("aMap"), new TypeToken<HashMap<String, List<Person>>>() {
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
