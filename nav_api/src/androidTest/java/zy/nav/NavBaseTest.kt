package zy.nav

import android.content.Context
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import zy.nav.route.TestActivityRegister
import zy.nav.route.TestFragmentRegister
import zy.nav.route.TestInterceptorRegister
import zy.nav.service.TestServiceRegister

/**
 * created by zhangyuan on 2020/5/28
 */
@RunWith(AndroidJUnit4::class)
abstract class NavBaseTest {

    internal var appContext: Context? = null

    @Before
    fun onBefore() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Intents.init()
        Nav.register(TestActivityRegister())
        NavRegistry.register(TestActivityRegister::class.java.name)
        Nav.register(TestFragmentRegister())
        NavRegistry.register(TestFragmentRegister::class.java.name)
        Nav.register(TestInterceptorRegister())
        NavRegistry.register(TestInterceptorRegister::class.java.name)
        Nav.register(TestServiceRegister())
        NavRegistry.register(TestServiceRegister::class.java.name)

        Assert.assertNotNull(NavRegistry.getActivity(TestConstants.PATH_TEST_ACTIVITY_1))
        Assert.assertNull(NavRegistry.getActivity(TestConstants.PATH_TEST_ACTIVITY_2))
        Assert.assertNotNull(NavRegistry.getFragment(TestConstants.PATH_TEST_FRAGMENT_1))
        Assert.assertNotNull(NavRegistry.getService(JsonMarshaller::class.java.name))
    }

    @After
    fun onAfter() {
        Intents.release()
    }
}