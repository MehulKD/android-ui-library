package com.github.badoualy.ui.util

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.github.badoualy.ui.listener.FragmentTransactionHandler
import com.github.badoualy.ui.listener.NavigationDrawerHandler
import com.github.badoualy.ui.listener.ToolbarHandler

fun Fragment.getAppliation() = activity?.application
fun Fragment.getAppCompatActivity() = activity as? AppCompatActivity
fun Fragment.getActionBar() = activity?.actionBar
fun Fragment.getSupportActionBar() = getAppCompatActivity()?.supportActionBar

fun Fragment.toolbar() = (activity as? ToolbarHandler)?.toolbar
fun Fragment.setHomeAsUp(homeAsUp: Boolean) = (activity as? ToolbarHandler)?.setHomeAsUpEnabled(
        homeAsUp)

fun Fragment.isDrawerLocked() = (activity as? NavigationDrawerHandler)?.drawerLocked
fun Fragment.lockDrawer(lock: Boolean) {
    (activity as? NavigationDrawerHandler)?.drawerLocked = lock
}

fun Fragment.finish() = activity?.fragmentManager?.popBackStack()
fun Fragment.postFinish() = activity?.runOnUiThread { fragmentManager?.popBackStack() }

fun Fragment.setTitle(title: String?) {
    (activity as? AppCompatActivity)?.supportActionBar?.title = title
}

fun Fragment.setTitle(titleRes: Int) {
    (activity as? AppCompatActivity)?.supportActionBar?.setTitle(titleRes)
}

fun Fragment.setSubtitle(subtitle: String?) {
    (activity as? AppCompatActivity)?.supportActionBar?.subtitle = subtitle
}

fun Fragment.setSubtitle(subtitleRes: Int) {
    (activity as? AppCompatActivity)?.supportActionBar?.setSubtitle(subtitleRes)
}

fun Fragment.displayFragment(fragment: Fragment, addToBackStack: Boolean = true, clearStack: Boolean = false) {
    (activity as? FragmentTransactionHandler)?.displayFragment(fragment, addToBackStack, clearStack)
}