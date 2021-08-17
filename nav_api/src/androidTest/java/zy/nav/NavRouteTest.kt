package zy.nav

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import zy.nav.exception.NavException
import zy.nav.exception.RedirectException
import zy.nav.route.TestNavActivity1
import zy.nav.route.TestNavActivity2
import zy.nav.route.TestNavFragment1
import zy.nav.util.Person

/**
 * created by zhangyuan on 2020/5/28
 */
@RunWith(AndroidJUnit4::class)
class NavRouteTest : NavBaseTest() {

    /**
     * 注解注册
     */
    @Test
    fun testActivity1() {
        val nav = Nav.from(appContext)
        addParams(nav)
        nav.addFlag(Intent.FLAG_ACTIVITY_NEW_TASK)
        nav.withInterceptor {
            val request = it.request()
            Assert.assertNotEquals(request.flags() and Intent.FLAG_ACTIVITY_NEW_TASK, 0)
            val response = it.process(request)
            Assert.assertNotEquals(request.flags() and Intent.FLAG_ACTIVITY_CLEAR_TOP, 0)
            Assert.assertTrue(response.success())
            Assert.assertTrue(TestNavActivity1::class.java == response.foundClass())
            assertParams(response.extras())
            response
        }
        nav.to(TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_1), 111)
        Intents.intended(IntentMatchers.hasComponent(TestNavActivity1::class.java.name))
    }

    /**
     * 清单文件注册
     */
    @Test
    fun testActivity2() {
        val nav = Nav.from(appContext)
        addParams(nav)
        nav.addFlag(Intent.FLAG_ACTIVITY_NEW_TASK)
        nav.withInterceptor {
            val request = it.request()
            Assert.assertNotEquals(request.flags() and Intent.FLAG_ACTIVITY_NEW_TASK, 0)
            val response = it.process(request)
            Assert.assertNotEquals(request.flags() and Intent.FLAG_ACTIVITY_CLEAR_TOP, 0)
            Assert.assertTrue(response.success())
            Assert.assertTrue(TestNavActivity2::class.java == response.foundClass())
            assertParams(response.extras())
            response
        }
        nav.to(TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_2))
        Intents.intended(IntentMatchers.hasComponent(TestNavActivity2::class.java.name))

    }

    /**
     * 拦截
     */
    @Test
    fun testActivity_intercept() {
        val nav = Nav.from(appContext)
        addParams(nav)
        nav.withInterceptor {
            kotlin.runCatching {
                it.process(it.request())
            }.onFailure {
                Assert.assertTrue(it is NavException)
            }.getOrNull()
        }
        nav.withInterceptor {
            val request = it.request()
            if (request.url().contains(TestConstants.PATH_TEST_ACTIVITY_1)) {
                request.intercept("test")
            }
            it.process(request)
        }
        nav.to(TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_1), 111)
    }

    /**
     * 拦截
     */
    @Test
    fun testActivity_redirect() {
        val nav = Nav.from(appContext)
        addParams(nav)
        nav.withInterceptor {
            kotlin.runCatching {
                it.process(it.request())
            }.onFailure {
                Assert.assertTrue(it is RedirectException)
            }.getOrNull()
        }
        nav.withInterceptor {
            val request = it.request()
            if (request.url().contains(TestConstants.PATH_TEST_ACTIVITY_2)) {
                request.redirect(TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_1))
            }
            val response = it.process(request)
            Assert.assertTrue(response.success())
            Assert.assertTrue(TestNavActivity1::class.java == response.foundClass())
            assertParams(response.extras())
            response
        }
        nav.to(TestConstants.url(TestConstants.PATH_TEST_ACTIVITY_2), 111)
    }

    @Test
    fun testFragment1() {
        val nav = Nav.from(appContext)
        addParams(nav)
        nav.withInterceptor {
            val response = it.process(it.request())
            Assert.assertTrue(response.success())
            Assert.assertTrue(TestNavFragment1::class.java == response.foundClass())
            assertParams(response.extras())
            response
        }
        val fragment = nav.getFragment(TestConstants.url(TestConstants.PATH_TEST_FRAGMENT_1))
        Assert.assertTrue(fragment is TestNavFragment1)
        assertParams(fragment?.arguments!!)

        val testNavFragment1 = fragment as TestNavFragment1
        Nav.inject(fragment)
        Assert.assertTrue(testNavFragment1.aBoolean ?: false)
        Assert.assertEquals(testNavFragment1.aByte, Byte.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.aShort, Short.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.anInt, Int.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.aLong, Long.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.aFloat, Float.MAX_VALUE)
        Assert.assertTrue(testNavFragment1.aDouble == Double.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.aChar, Char.MAX_VALUE)
        Assert.assertEquals(testNavFragment1.aString, "String.MAX_VALUE")
        val json = Nav.getService(JsonMarshaller::class.java)
        Assert.assertEquals(
            json?.toJson(testNavFragment1.anObject),
            json?.toJson(Person.testData())
        )
        Assert.assertEquals(json?.toJson(testNavFragment1.aMap), json?.toJson(Person.testDataMap()))
    }

    @Test
    fun testFragment_null() {
        Nav.from(appContext)
            .withInterceptor {
                val response = it.process(it.request())
                Assert.assertFalse(response.success())
                Assert.assertNull(response.foundClass())
                response
            }
            .getFragment("")
    }

    private fun addParams(nav: Nav) {
        nav.withBoolean("aBoolean", true)
            .withByte("aByte", Byte.MAX_VALUE)
            .withShort("aShort", Short.MAX_VALUE)
            .withInt("anInt", Int.MAX_VALUE)
            .withLong("aLong", Long.MAX_VALUE)
            .withFloat("aFloat", Float.MAX_VALUE)
            .withDouble("aDouble", Double.MAX_VALUE)
            .withChar("aChar", Char.MAX_VALUE)
            .withString("aString", "String.MAX_VALUE")
            .withObject("anObject", Person.testData())
            .withObject("aMap", Person.testDataMap())
    }

    private fun assertParams(bundle: Bundle) {
        Assert.assertTrue(bundle.getBoolean("aBoolean"))
        Assert.assertEquals(bundle.getByte("aByte"), Byte.MAX_VALUE)
        Assert.assertEquals(bundle.getShort("aShort"), Short.MAX_VALUE)
        Assert.assertEquals(bundle.getInt("anInt"), Int.MAX_VALUE)
        Assert.assertEquals(bundle.getLong("aLong"), Long.MAX_VALUE)
        Assert.assertEquals(bundle.getFloat("aFloat"), Float.MAX_VALUE)
        Assert.assertTrue(bundle.getDouble("aDouble") == Double.MAX_VALUE)
        Assert.assertEquals(bundle.getChar("aChar"), Char.MAX_VALUE)
        Assert.assertEquals(bundle.getString("aString"), "String.MAX_VALUE")
        Assert.assertEquals(
            bundle.getString("anObject"),
            Nav.getService(JsonMarshaller::class.java)?.toJson(Person.testData())
        )
        Assert.assertEquals(
            bundle.getString("aMap"),
            Nav.getService(JsonMarshaller::class.java)?.toJson(Person.testDataMap())
        )
    }

}