package com.github.badoualy.ui.util

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.github.badoualy.ui.config.AppConfig

fun Activity.newDialog(): MaterialDialog.Builder {
    val builder = MaterialDialog.Builder(this)
    val application = application
    if (application is AppConfig) {
        builder.positiveColor(application.accentColor)
                .negativeColor(application.accentColor)
    }

    return builder
}