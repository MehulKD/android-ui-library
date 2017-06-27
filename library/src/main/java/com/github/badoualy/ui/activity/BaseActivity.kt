package com.github.badoualy.ui.activity

import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.badoualy.ui.listener.Backable
import com.github.badoualy.ui.listener.FragmentTransactionHandler

abstract class BaseActivity : AppCompatActivity(), FragmentTransactionHandler {

    protected val TAG = javaClass.simpleName!!

    var lifeCycleListener: ActivityLifeCycleListener? = null
    open val fragmentContainerId: Int
        get() = 0
    val currentFragment: Fragment?
        get() = fragmentManager.findFragmentById(fragmentContainerId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifeCycleListener?.onActivityCreated()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifeCycleListener?.onActivityResulted(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        lifeCycleListener?.onActivityResumed()
    }

    override fun onPause() {
        lifeCycleListener?.onActivityPaused()
        super.onPause()
    }

    override fun onStop() {
        lifeCycleListener?.onActivityStopped()
        super.onStop()
    }

    /**
     * Override default behavior to process event in fragment first if instance of BaseFragment, then the activity if
     * needed, and the default behavior if none consumed the event
     */
    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment != null && fragment is Backable) {
            if (fragment.onBackPressed()) {
                return
            }
        }

        if (onBackPressedAfterFragment()) {
            return
        }

        super.onBackPressed()
    }

    /**
     * Called when back button is pressed and if the fragment displayed didn't consume the event

     * @return true if the event was consumed
     */
    protected fun onBackPressedAfterFragment() = false

    override fun displayFragment(fragment: Fragment, addToBackStack: Boolean, clearStack: Boolean) {
        val fm = fragmentManager
        if (clearStack && fm.backStackEntryCount > 0) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val ft = fm.beginTransaction()
        ft.replace(fragmentContainerId, fragment)
        if (addToBackStack) {
            ft.addToBackStack(null)
        }
        ft.commitAllowingStateLoss()
    }

    override fun executeTransaction(transactionRunnable: FragmentTransactionHandler.FragmentTransactionRunnable) {
        transactionRunnable.run(fragmentManager, fragmentContainerId)
    }

    fun removeFragment() {
        if (currentFragment != null) {
            fragmentManager.beginTransaction().remove(currentFragment).commit()
        }
    }

    interface ActivityLifeCycleListener {
        fun onActivityCreated()
        fun onActivityResulted(requestCode: Int, resultCode: Int, data: Intent?)
        fun onActivityResumed()
        fun onActivityPaused()
        fun onActivityStopped()
    }
}
