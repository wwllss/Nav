package zy.nav.route

import org.junit.Assert
import zy.nav.Interceptor
import zy.nav.Response
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/29
 */
class TestNavActivity2Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        Assert.assertEquals(request?.url(), TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_2))
        return chain?.process(request)!!
    }

}