package zy.nav.route

import zy.nav.FragmentRegister
import zy.nav.TestConstants

/**
 * created by zhangyuan on 2020/5/28
 */
class TestFragmentRegister : FragmentRegister {

    override fun register(activities: MutableMap<String, String>?) {
        activities?.put(TestConstants.PATH_TEST_FRAGMENT_1, TestNavFragment1::class.java.name)
    }

}