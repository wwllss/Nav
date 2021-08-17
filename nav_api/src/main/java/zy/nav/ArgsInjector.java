package zy.nav;

import java.util.Map;

public interface ArgsInjector {

    void inject(Object target);

    Map<String, Integer> getParamTypes();

}
