package zy.nav.route

import android.content.Intent
import org.junit.Assert
import zy.nav.Interceptor
import zy.nav.Response
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/29
 */
class TestNavRouteInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        request?.addFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val urlArr = arrayOf(
            TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_1),
            TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_2),
            TestConstants.url(TestConstants.PATH_TEST_FRAGMENT_1)
        )
        Assert.assertTrue(urlArr.contains(request?.url()))
        return chain?.process(request)!!
    }

}