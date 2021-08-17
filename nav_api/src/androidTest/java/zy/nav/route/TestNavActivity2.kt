package zy.nav.route

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView

/**
 * created by zhangyuan on 2020/5/28
 */
class TestNavActivity2 : TestNavActivity1() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            gravity = Gravity.CENTER
            text = TestNavActivity2::class.java.simpleName
            background = ColorDrawable(Color.RED)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("onActivityResult", true)
        })
        super.finish()
    }
}