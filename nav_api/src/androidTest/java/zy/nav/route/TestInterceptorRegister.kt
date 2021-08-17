package zy.nav.route

import zy.nav.InterceptorRegister
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/29
 */
class TestInterceptorRegister : InterceptorRegister {

    override fun register(interceptors: MutableMap<String, MutableMap<Int, String>>?) {
        interceptors?.apply {
            this[""] = mutableMapOf(0 to TestNavRouteInterceptor::class.java.name)
            this[TestConstants.PATH_TEST_ACTIVITY_1] =
                mutableMapOf(0 to TestNavActivity1Interceptor::class.java.name)
            this[TestConstants.PATH_TEST_ACTIVITY_2] =
                mutableMapOf(0 to TestNavActivity2Interceptor::class.java.name)
            this[TestConstants.PATH_TEST_FRAGMENT_1] =
                mutableMapOf(0 to TestNavFragment1Interceptor::class.java.name)
        }
    }

}