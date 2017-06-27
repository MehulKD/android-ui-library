package com.github.badoualy.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.github.badoualy.ui.util.getApplicationVersionName

/**
 * Provide a simple implementation for basic feature of an application's main activity, like displaying a splash screen
 * or a change log Only
 * the main activity class (launcher activity) should override this class
 */
abstract class BaseMainActivity : BaseActivity() {

    var launchCount: Long = 0
        private set
    var resumeCount: Long = 0
        private set
    private lateinit var lastLaunchVersion: String
    private lateinit var version: String

    /**
     * Note that you shouldn't load anything in onCreate, as no Be-Store variant may be installed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE)
        launchCount = preferences.getLong(KEY_LAUNCH_COUNT, 0)
        version = getApplicationVersionName().orEmpty()
        lastLaunchVersion = preferences.getString(KEY_LAST_LAUNCH_VERSION, version)
        incrementLaunchCount(preferences)

        if (launchCount > 0 && !version.equals(lastLaunchVersion, ignoreCase = true)) {
            onApplicationUpdated(lastLaunchVersion, version)
        }
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE)
        resumeCount = prefs.getLong(KEY_RESUME_COUNT, 1)
        incrementResumeCount(prefs)
        updateLastLaunchVersion(prefs)
    }

    @SuppressLint("CommitPrefEdits")
    private fun incrementLaunchCount(preferences: SharedPreferences): Long {
        preferences.edit().putLong(KEY_LAUNCH_COUNT, launchCount + 1).commit()
        Log.v(TAG, "launchCount = " + launchCount)
        return launchCount
    }

    @SuppressLint("CommitPrefEdits")
    private fun incrementResumeCount(preferences: SharedPreferences): Long {
        preferences.edit().putLong(KEY_RESUME_COUNT, resumeCount + 1).commit()
        Log.v(TAG, "resumeCount = " + resumeCount)
        return resumeCount
    }

    /**
     * Update the value of the preference saving the last launch application's version
     */
    private fun updateLastLaunchVersion(preferences: SharedPreferences) {
        preferences.edit().putString(KEY_LAST_LAUNCH_VERSION, version).apply()
    }

    /**
     * Reset the launch count of this application to 0
     */
    protected fun resetLaunchCount() {
        getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE)
                .edit()
                .remove(KEY_LAUNCH_COUNT)
                .remove(KEY_RESUME_COUNT)
                .apply()
    }

    protected fun onApplicationUpdated(oldVersion: String, currentVersion: String) {

    }

    companion object {
        private val PREFERENCES_APP = "AppPreferences"
        private val KEY_LAUNCH_COUNT = "launchCount"
        private val KEY_RESUME_COUNT = "resumeCount"
        private val KEY_LAST_LAUNCH_VERSION = "lastLaunchVersion"
    }
}
