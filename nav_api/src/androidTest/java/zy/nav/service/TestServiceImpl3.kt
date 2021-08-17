package zy.nav.service

import zy.nav.util.Person

/**
 * created by zhangyuan on 2020/5/29
 */
class TestServiceImpl3(
    var anInt: Int?,
    var aString: String?,
    var anObject: Person?
) : AbstractTestService() {

    override fun doSomething() {

    }

}