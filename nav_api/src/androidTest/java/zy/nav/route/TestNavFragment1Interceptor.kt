package zy.nav.route

import org.junit.Assert
import zy.nav.Request
import zy.nav.Response
import zy.nav.SimpleInterceptor
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/29
 */
class TestNavFragment1Interceptor : SimpleInterceptor() {

    override fun onInterceptBefore(request: Request?): Boolean {
        Assert.assertEquals(request?.url(), TestConstants.url(TestConstants.PATH_TEST_FRAGMENT_1))
        return super.onInterceptBefore(request)
    }

    override fun onInterceptAfter(request: Request?, response: Response?): Boolean {
        Assert.assertTrue(response?.foundClass() == TestNavFragment1::class.java)
        return super.onInterceptAfter(request, response)
    }

}