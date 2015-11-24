package com.github.badoualy.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.badoualy.ui.R;
import com.github.badoualy.ui.config.ChangeLogProvider;
import com.github.badoualy.ui.config.SplashConfig;
import com.github.badoualy.ui.model.ChangeLog;

import java.util.ArrayList;
import java.util.List;

/**
 Provide a simple implementation for basic feature of an application's main activity, like displaying a splash screen or a change log Only
 the main activity class (launcher activity) should override this class
 */
public abstract class BaseMainActivity extends BaseActivity implements SplashConfig, ChangeLogProvider {

    public static final int REQUEST_CODE_SPLASH = 1992;

    private static final int RATE_APP_DIALOG_FREQUENCY = 10;

    private static final String PREFERENCES_APP = "AppPreferences";
    private static final String KEY_LAUNCH_COUNT = "launchCount";
    private static final String KEY_RESUME_COUNT = "resumeCount";
    private static final String KEY_LAST_LAUNCH_VERSION = "lastLaunchVersion";

    private int launchCount;
    private int resumeCount;
    private String lastLaunchVersion;
    private String version;

    private boolean callingSplash = false;
    private boolean callingRateDialog = false;

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

            if (!lastLaunchVersion.equalsIgnoreCase(version)) {
                Log.d(TAG, "Updated from " + lastLaunchVersion + " to " + version);
                String[] versionFields = version.split(" ")[0].split("\\.");
                if (versionFields.length < 3) {
                    Log.e(TAG, "Version name has an incorrect format! " + version);
                    return;
                }

                String[] lastVersionFields = lastLaunchVersion.split(" ")[0].split("\\.");
                if (lastVersionFields.length < 3) {
                    Log.e(TAG, "Last version name has an incorrect format! " + lastLaunchVersion);
                    return;
                }

                showChangeLogDialog();
            }
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

    @Override
    protected boolean onBackPressedAfterFragment() {
//        if (!callingRateDialog && getResumeCount() >= RATE_APP_DIALOG_FREQUENCY && getResumeCount() % RATE_APP_DIALOG_FREQUENCY == 0) {
//            callingRateDialog = true;
//            new MaterialDialog.Builder(this)
//                    .title(R.string.dialog_changelog_title)
//                    .content(R.string.dialog_cancel)
//                    .positiveText(R.string.dialog_ok)
//                    .negativeText(R.string.dialog_not_now)
//                    .cancelable(true)
//                    .callback(new MaterialDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(MaterialDialog dialog) {
//                            openPlayStore(getPackageName());
//                        }
//
//                        @Override
//                        public void onNegative(MaterialDialog dialog) {
//                            onBackPressed();
//                        }
//                    })
//                    .show();
//            return true;
//        }


        return super.onBackPressedAfterFragment();
    }

    /**
     Increment the launch counter of this BeApp

     @return the current launch count (before incrementation)
     */
    @SuppressLint("CommitPrefEdits")
    private int incrementLaunchCount(SharedPreferences preferences) {
        preferences.edit().putInt(KEY_LAUNCH_COUNT, launchCount + 1).commit();

        Log.d(TAG, "launchCount = " + launchCount);
        return launchCount;
    }

    /**
     Increment the resume counter of this BeApp

     @return the current launch count (before incrementation)
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

    private void showChangeLogDialog() {
        List<ChangeLog> changeLogs = getRelevantChangeLogs();
        if (!changeLogs.isEmpty()) {
            StringBuilder contentBuilder = new StringBuilder();
            for (ChangeLog changeLog : changeLogs) {
                contentBuilder.append(changeLog.toHtmlString()).append("<br>");
            }
            String content = contentBuilder.toString();
            content = content.substring(0, content.length() - 8);

            new MaterialDialog.Builder(this)
                    .title(R.string.dialog_changelog_title)
                    .content(Html.fromHtml(content))
                    .positiveText(R.string.dialog_ok)
                    .cancelable(true)
                    .show();
        } else
            Log.d(TAG, "No change log available");
    }

    private List<ChangeLog> getRelevantChangeLogs() {
        List<ChangeLog> changeLogsFull = ((ChangeLogProvider) this).getChangeLogs();
        List<ChangeLog> changeLogs = new ArrayList<>();

        if (changeLogsFull != null) {
            String[] lastLaunchVersionFields = lastLaunchVersion.split(" ")[0].split("\\.");
            int lastMajor = Integer.parseInt(lastLaunchVersionFields[0]);
            int lastMinor = Integer.parseInt(lastLaunchVersionFields[1]);
            int lastHotfix = Integer.parseInt(lastLaunchVersionFields[2]);

            for (ChangeLog log : changeLogsFull) {
                if (log.versionMajor > lastMajor || (log.versionMajor == lastMajor && log.versionMinor > lastMinor)
                        || (log.versionMajor == lastMajor && log.versionMinor == lastMinor && log.versionHotfix > lastHotfix))
                    changeLogs.add(log);
                else
                    break;
            }
        }

        return changeLogs;
    }

    protected void setCallingSplash(boolean callingSplash) {
        Log.d(TAG, "setCallingSplash " + callingSplash);
        this.callingSplash = callingSplash;
    }

    public boolean isCallingSplash() {
        return callingSplash;
    }
}
