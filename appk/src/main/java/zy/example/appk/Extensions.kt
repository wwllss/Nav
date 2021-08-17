package zy.example.appk

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import zy.nav.Nav

infix fun Context.push(url: String) {
    Nav.from(this).to(url)
}

infix fun Activity.push(url: String) {
    Nav.from(this).to(url)
}

infix fun Fragment.push(url: String) {
    Nav.from(this).to(url)
}