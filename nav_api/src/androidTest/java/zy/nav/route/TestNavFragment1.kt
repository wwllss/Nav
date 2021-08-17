package zy.nav.route

import androidx.fragment.app.Fragment
import zy.nav.TestConstants
import zy.nav.annotation.Arg
import zy.nav.annotation.Route
import zy.nav.util.Person

/**
 * created by zhangyuan on 2020/5/28
 */
@Route(TestConstants.PATH_TEST_FRAGMENT_1)
class TestNavFragment1 : Fragment() {

    @Arg
    @JvmField
    var aBoolean: Boolean? = false

    @Arg
    @JvmField
    var aByte: Byte? = -1

    @Arg
    @JvmField
    var aShort: Short? = -1

    @Arg
    @JvmField
    var anInt: Int? = -1

    @Arg
    @JvmField
    var aLong: Long? = -1

    @Arg
    @JvmField
    var aFloat: Float? = (-1).toFloat()

    @Arg
    @JvmField
    var aDouble: Double? = (-1).toDouble()

    @Arg
    @JvmField
    var aChar: Char? = '-'

    @Arg
    @JvmField
    var aString: String? = ""

    @Arg
    @JvmField
    var anObject: Person? = null

    @Arg
    @JvmField
    var aMap: HashMap<String, List<Person>>? = null
}