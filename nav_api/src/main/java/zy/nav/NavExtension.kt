package zy.nav

import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment

typealias NavBlock = (Nav) -> Unit

fun Context?.nav(url: String?, @IntRange(from = 1) requestCode: Int? = null, block: NavBlock = {}) {
    if (this == null || url.isNullOrEmpty()) {
        return
    }
    val nav = Nav.from(this)
    block(nav)
    if (requestCode == null) {
        nav.to(url)
    } else {
        nav.to(url, requestCode)
    }
}

fun Fragment?.nav(
    url: String?,
    @IntRange(from = 1) requestCode: Int? = null,
    block: NavBlock = {}
) {
    if (this == null || url.isNullOrEmpty()) {
        return
    }
    val nav = Nav.from(this)
    block(nav)
    if (requestCode == null) {
        nav.to(url)
    } else {
        nav.to(url, requestCode)
    }
}