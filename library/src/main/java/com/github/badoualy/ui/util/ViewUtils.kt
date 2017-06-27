package com.github.badoualy.ui.util

import android.view.View
import android.widget.TextView

fun View.gone() {
    visibility = View.GONE
}

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.invisible(invisible: Boolean = true) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

fun View.isGone() = visibility == View.GONE
fun View.isInvisible() = visibility == View.INVISIBLE
fun View.isVisible() = visibility == View.VISIBLE

fun TextView.postText(string: String) = post { text = string }
fun TextView.postText(res: Int) = post { setText(res) }