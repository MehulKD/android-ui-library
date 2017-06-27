package com.github.badoualy.ui.listener


import android.app.Fragment
import android.app.FragmentManager

interface FragmentTransactionHandler {

    fun executeTransaction(transactionRunnable: FragmentTransactionRunnable)

    fun displayFragment(fragment: Fragment, addToBackStack: Boolean = true, clearStack: Boolean = false)

    /** A custom runnable that will operate a FragmentTransaction  */
    interface FragmentTransactionRunnable {
        fun run(fm: FragmentManager, fragmentId: Int)
    }
}
