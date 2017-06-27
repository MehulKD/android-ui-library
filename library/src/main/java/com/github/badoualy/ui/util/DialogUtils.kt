package com.github.badoualy.ui.util

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.github.badoualy.ui.config.AppConfig

fun MaterialDialog.newDialog(activity: Activity): MaterialDialog.Builder {
    val builder = MaterialDialog.Builder(activity)
    val application = activity.application
    if (application is AppConfig) {
        builder.positiveColor(application.accentColor)
                .negativeColor(application.accentColor)
    }

    return builder
}