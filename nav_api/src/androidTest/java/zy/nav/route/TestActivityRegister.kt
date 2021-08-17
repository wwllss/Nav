package zy.nav.route

import zy.nav.ActivityRegister
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/28
 */
class TestActivityRegister : ActivityRegister {

    override fun register(activities: MutableMap<String, String>?) {
        activities?.put(TestConstants.PATH_TEST_ACTIVITY_1, TestNavActivity1::class.java.name)
    }

}