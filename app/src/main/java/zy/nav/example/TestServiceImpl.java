package zy.nav.example;

import zy.example.app_service.TestService;
import zy.nav.annotation.Service;

/**
 * created by zhangyuan on 2020/4/15
 */
@Service(value = TestService.class)
public class TestServiceImpl implements TestService {

    private final int token;

    public TestServiceImpl(int token, String s) {
        this.token = token;
    }

    @Override
    public void test() {
    }
}
