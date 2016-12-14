package com.github.badoualy.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.github.badoualy.ui.config.SplashConfig;

/**
 * Provide a simple implementation for basic feature of an application's main activity, like displaying a splash screen or a change log Only
 * the main activity class (launcher activity) should override this class
 */
public abstract class BaseMainActivity extends BaseActivity implements SplashConfig {

    public static final int REQUEST_CODE_SPLASH = 1992;

    private static final String PREFERENCES_APP = "AppPreferences";
    private static final String KEY_LAUNCH_COUNT = "launchCount";
    private static final String KEY_RESUME_COUNT = "resumeCount";
    private static final String KEY_LAST_LAUNCH_VERSION = "lastLaunchVersion";

    private int launchCount;
    private int resumeCount;
    private String lastLaunchVersion;
    private String version;

    private boolean callingSplash = false;

    /** Note that you shouldn't load anything in onCreate, as no Be-Store variant may be installed */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callingSplash = false;

        SharedPreferences preferences = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE);
        launchCount = preferences.getInt(KEY_LAUNCH_COUNT, 0);
        version = getApplicationVersionName();
        lastLaunchVersion = preferences.getString(KEY_LAST_LAUNCH_VERSION, version);
        incrementLaunchCount(preferences);

        if (launchCount == 0) {
            setCallingSplash(true);
            Log.d(TAG, "Launch count is 0, will call startSplash()");
            startSplash();
        } else if (!version.equalsIgnoreCase(lastLaunchVersion)) {
            onApplicationUpdated(lastLaunchVersion, version);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!callingSplash) {
            SharedPreferences prefs = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE);
            resumeCount = prefs.getInt(KEY_RESUME_COUNT, 1);
            incrementResumeCount(prefs);
            updateLastLaunchVersion(prefs);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + requestCode + " " + resultCode);
        if (requestCode == REQUEST_CODE_SPLASH) {
            Log.d(TAG, "Returned from splash screen with result " + resultCode);
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Canceled result received from splash, finishing");
                resetLaunchCount();
                finish();
                return;
            }

            setCallingSplash(false); // Only override for splash
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Increment the launch counter of this BeApp
     *
     * @return the current launch count (before incrementation)
     */
    @SuppressLint("CommitPrefEdits")
    private int incrementLaunchCount(SharedPreferences preferences) {
        preferences.edit().putInt(KEY_LAUNCH_COUNT, launchCount + 1).commit();

        Log.d(TAG, "launchCount = " + launchCount);
        return launchCount;
    }

    /**
     * Increment the resume counter of this BeApp
     *
     * @return the current launch count (before incrementation)
     */
    @SuppressLint("CommitPrefEdits")
    private int incrementResumeCount(SharedPreferences preferences) {
        preferences.edit().putInt(KEY_RESUME_COUNT, resumeCount + 1).commit();

        Log.d(TAG, "resumeCount = " + resumeCount);
        return resumeCount;
    }

    /** Update the value of the preference saving the last launch application's version */
    private void updateLastLaunchVersion(SharedPreferences preferences) {
        preferences.edit().putString(KEY_LAST_LAUNCH_VERSION, version).apply();
    }

    /** @return The total launch count of this application */
    public int getLaunchCount() {
        return launchCount;
    }

    /** @return The total resume count of this application */
    public int getResumeCount() {
        return resumeCount;
    }

    public String getLastLaunchVersion() {
        return lastLaunchVersion;
    }

    /** Reset the launch count of this application to 0 */
    protected void resetLaunchCount() {
        getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE)
                .edit()
                .remove(KEY_LAUNCH_COUNT)
                .remove(KEY_RESUME_COUNT)
                .apply();
    }

    /** Start the splash screen with {@link BaseMainActivity#REQUEST_CODE_SPLASH} request code */
    protected final void startSplash() {
        SplashConfig splashConfig = this;
        Log.d(TAG, "Starting splashActivity");
        Intent intent = splashConfig.getSplashActivityIntent();
        if (intent == null) {
            Log.e(TAG, "getSplashActivityIntent is null, skipping splash step");
            setCallingSplash(false);
            return;
        }
        startActivityForResult(intent, REQUEST_CODE_SPLASH);
    }

    protected void setCallingSplash(boolean callingSplash) {
        Log.d(TAG, "setCallingSplash " + callingSplash);
        this.callingSplash = callingSplash;
    }

    public boolean isCallingSplash() {
        return callingSplash;
    }

    protected void onApplicationUpdated(String oldVersion, String currentVersion) {

    }
}
