package com.github.badoualy.ui.util

import android.app.Activity
import android.app.Fragment
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.inputmethod.InputMethodManager

fun Activity.closeKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let { focus ->
        focus.windowToken?.let { token ->
            inputMethodManager.hideSoftInputFromWindow(token, 0)
        }
    }
}

fun Activity.finish(result: Int) {
    setResult(result)
    finish()
}

fun Activity.postFinish() = runOnUiThread { finish() }
fun Activity.postFinish(result: Int = Activity.RESULT_OK) = runOnUiThread {
    setResult(result)
    finish()
}

fun Activity.requestPermission(permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}

fun Activity.hasPermission(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


////////// Delegates //////////
fun Fragment.closeKeyboard() = activity?.closeKeyboard()