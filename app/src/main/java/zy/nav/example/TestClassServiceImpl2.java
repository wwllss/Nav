package zy.nav.example;

import zy.example.app_service.TestClassService;
import zy.nav.annotation.Service;

/**
 * created by zhangyuan on 2020/4/15
 */
@Service(value = TestClassService.class, token = "2")
public class TestClassServiceImpl2 extends TestClassService {

    private final String token;

    private final Callback callback;

    public TestClassServiceImpl2(String token, Callback callback) {
        this.token = token;
        this.callback = callback;
    }

    @Override
    public void test() {
        System.out.println("TestClassServiceImpl token " + token);
    }
}
