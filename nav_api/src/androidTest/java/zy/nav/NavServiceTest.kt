package zy.nav

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import zy.nav.service.*
import zy.nav.util.Person

/**
 * created by zhangyuan on 2020/5/28
 */
@RunWith(AndroidJUnit4::class)
class NavServiceTest : NavBaseTest() {

    @Test
    fun testService() {
        Nav.getService(TestService::class.java).apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl1)
        }
        Nav.getService(TestService::class.java, "2").apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl2)
        }
        Nav.getService(AbstractTestService::class.java, "3").apply {
            Assert.assertNull(this)
        }
        Nav.getService(AbstractTestService::class.java, "3", 1, "2", Person.testData()).apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl3)
        }
        Nav.getService(AbstractTestService::class.java, "3", 1, "2", null).apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl3)
        }
        Nav.getService(AbstractTestService::class.java, "3", 1, null, Person.testData()).apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl3)
        }
        Nav.getService(AbstractTestService::class.java, "3", null, "2", Person.testData()).apply {
            Assert.assertNotNull(this)
            Assert.assertTrue(this is TestServiceImpl3)
        }
    }

}