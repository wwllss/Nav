package zy.nav.service

import zy.nav.JsonMarshaller
import zy.nav.ServiceRegister

/**
 * created by zhangyuan on 2020/5/28
 */
class TestServiceRegister : ServiceRegister {

    override fun register(services: MutableMap<String, String>?) {
        services?.put(JsonMarshaller::class.java.name, TestJsonServiceImpl::class.java.name)
        services?.put(TestService::class.java.name, TestServiceImpl1::class.java.name)
        services?.put("${TestService::class.java.name}-2", TestServiceImpl2::class.java.name)
        services?.put(
            "${AbstractTestService::class.java.name}-3",
            TestServiceImpl3::class.java.name
        )
    }

}