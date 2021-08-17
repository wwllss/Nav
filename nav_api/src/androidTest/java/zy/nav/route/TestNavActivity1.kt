package zy.nav.route

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import zy.nav.Nav
import zy.nav.TestConstants
import zy.nav.annotation.Arg
import zy.nav.annotation.Route
import zy.nav.util.Person

/**
 * created by zhangyuan on 2020/5/28
 */
@Route(TestConstants.PATH_TEST_ACTIVITY_1)
open class TestNavActivity1 : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            gravity = Gravity.CENTER
            text = TestNavActivity1::class.java.simpleName
            background = ColorDrawable(Color.BLUE)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
        Nav.inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode && requestCode == 111) {
            if (data?.getBooleanExtra("onActivityResult", false) == true) {
                Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show()
            }
        }
    }

}