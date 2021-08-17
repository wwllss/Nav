package zy.nav.example;

import zy.example.app_service.TestService;
import zy.nav.annotation.Service;

/**
 * created by zhangyuan on 2020/4/15
 */
@Service(value = TestService.class, token = "2")
public class TestServiceImpl2 implements TestService {

    private final Integer token;

    public TestServiceImpl2(Integer token, String s) {
        this.token = token;
    }

    @Override
    public void test() {
    }
}
