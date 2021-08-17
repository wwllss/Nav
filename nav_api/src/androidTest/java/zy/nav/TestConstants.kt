package zy.nav

/**
 * created by zhangyuan on 2020/5/28
 */
object TestConstants {

    const val SCHEME = "nav"

    const val HOST = "test"

    const val PATH_TEST_ACTIVITY_1 = "/activity/1"

    const val PATH_TEST_ACTIVITY_2 = "/activity/2"

    const val PATH_TEST_FRAGMENT_1 = "/fragment/1"

    fun url(path: String) = "$SCHEME://$HOST$path"

}