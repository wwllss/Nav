package zy.nav.example;

import zy.example.app_service.TestClassService;
import zy.nav.annotation.Service;

/**
 * created by zhangyuan on 2020/4/15
 */
@Service(value = TestClassService.class, token = "1")
public class TestClassServiceImpl extends TestClassService {

    @Override
    public void test() {
        System.out.println("TestClassServiceImpl token 1");
    }

    @Service(value = TestClassService.class, token = "inner")
    public static class InnerTestClassServiceImpl extends TestClassService {

        @Override
        public void test() {
            System.out.println("InnerTestClassServiceImpl");
        }
    }
}
