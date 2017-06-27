package com.github.badoualy.ui.util

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri

fun Context.getApplicationVersionName(): String? {
    try {
        return packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return null
}

fun Context.getApplicationName() = applicationInfo.name!!

fun Context.openPlayStore(packageName: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW,
                             Uri.parse("market://details?id=" + packageName)))
    } catch (e: ActivityNotFoundException) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                                 Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
        } catch (e2: ActivityNotFoundException) {

        }

    }
}

fun Context.openApplication(packageName: String) {
    startActivity(packageManager.getLaunchIntentForPackage(packageName))
}

fun Context.copyToClipboard(tag: String, value: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(tag, value)
    clipboard.primaryClip = clip
}

fun Context.hasLowStorage() =
        registerReceiver(null, IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)) != null

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}