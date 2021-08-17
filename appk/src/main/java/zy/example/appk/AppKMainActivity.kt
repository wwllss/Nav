package zy.example.appk

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import zy.nav.Nav
import zy.nav.annotation.Arg
import zy.nav.annotation.Route
import zy.nav.nav

@Route("/app/k/main")
class AppKMainActivity : AppCompatActivity() {

    @Arg("kotlin_msg")
    @JvmField
    var msg: String = ""

    @Arg("anInt")
    @JvmField
    var anInt: Int? = null

    @Arg("aChar")
    @JvmField
    var aChar: Char? = null

    @Arg("aFloat")
    @JvmField
    var aFloat: Float? = null

    @Arg("aDouble")
    @JvmField
    var aDouble: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_k_main)
        Nav.inject(this)
        val text = findViewById<TextView>(R.id.text)
        text.text = msg
        text.setOnClickListener {
            Nav.from(this)
                .addFlag(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .to("/app/main")
            finish()
        }


        nav("/app/main")
        this push "/app/main"
    }
}
