package com.github.badoualy.ui.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.badoualy.ui.R;
import com.github.badoualy.ui.exception.ClearDataException;
import com.github.badoualy.ui.fragment.BaseFragment;
import com.github.badoualy.ui.listener.FragmentTransactionHandler;
import com.github.badoualy.ui.listener.SnackHandler;

public abstract class BaseActivity extends AppCompatActivity implements FragmentTransactionHandler, SnackHandler {

    protected final String TAG = getClass().getSimpleName();
    /**
     EventBus default priority, lower than BaseFragment.DEFAULT_PRIORITY
     */
    protected final int DEFAULT_PRIORITY = 200;

    private ActivityLifeCycleListener lifeCycleListener;

    public void setLifeCycleListener(ActivityLifeCycleListener lifeCycleListener) {
        this.lifeCycleListener = lifeCycleListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lifeCycleListener != null)
            lifeCycleListener.onActivityCreated();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (lifeCycleListener != null)
            lifeCycleListener.onActivityResulted(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lifeCycleListener != null)
            lifeCycleListener.onActivityResumed();
    }

    @Override
    protected void onPause() {
        if (lifeCycleListener != null)
            lifeCycleListener.onActivityPaused();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (lifeCycleListener != null)
            lifeCycleListener.onActivityStopped();
        super.onStop();
    }

    /**
     Close the soft input keyboard if open
     */
    public final void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            IBinder token = focus.getWindowToken();
            if (token != null)
                inputMethodManager.hideSoftInputFromWindow(token, 0);
        }
    }

    /**
     Override default behavior to process event in fragment first if instance of BaseFragment, then the activity if
     needed, and the default behavior if none consumed the event
     */
    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if (fragment != null && fragment instanceof BaseFragment) {
            if (((BaseFragment) fragment).onBackPressed())
                return;
        }

        if (onBackPressedAfterFragment())
            return;

        super.onBackPressed();
    }

    /**
     Called when back button is pressed and if the fragment displayed didn't consume the event

     @return true if the event was consumed
     */
    protected boolean onBackPressedAfterFragment() {
        return false;
    }

    /**
     Clear user data, this will ForceClose the app
     */
    public void clearUserData() {
        for (String s : databaseList()) {
            deleteDatabase(s);
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent
                .getActivity(this, mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, mPendingIntent);
        Toast.makeText(this, "Cleared user data. Reopening in 2 secs...", Toast.LENGTH_LONG).show();
        throw new ClearDataException();
    }

    /**
     @return {@link AppCompatActivity#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(int id) {
        return (T) super.findViewById(id);
    }

    /**
     @return {@link AppCompatActivity#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(int id, Class<T> clazz) {
        return (T) super.findViewById(id);
    }

    /**
     @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(View root, int id) {
        return (T) root.findViewById(id);
    }

    /**
     @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(View root, int id, Class<T> clazz) {
        return (T) root.findViewById(id);
    }

    /**
     @return the id of the main fragment container for this activity
     */
    public abstract int getFragmentContainerId();

    /**
     Call {@link BaseActivity#displayFragment(Fragment, boolean, boolean)} with addToBackStack = true and clearBack =
     false
     */
    @Override
    public final void displayFragment(Fragment fragment) {
        displayFragment(fragment, true, false);
    }

    /**
     Call {@link BaseActivity#displayFragment(Fragment, boolean, boolean)} with clearBack = false
     */
    @Override
    public final void displayFragment(Fragment fragment, boolean addToBackStack) {
        displayFragment(fragment, addToBackStack, false);
    }

    /**
     Display the given fragment in the {@link BaseActivity#getFragmentContainerId()}

     @param fragment       fragment to display
     @param addToBackStack should the transaction be added in the stack
     @param clearStack     should the back stack be cleared before the execution of the transaction
     */
    @Override
    public final void displayFragment(Fragment fragment, boolean addToBackStack, boolean clearStack) {
        FragmentManager fm = getSupportFragmentManager();
        if (clearStack && fm.getBackStackEntryCount() > 0)
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(getFragmentContainerId(), fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     @return the current fragment displayed in {@link BaseActivity#getFragmentContainerId()}
     */
    public final Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(getFragmentContainerId());
    }

    /**
     Remove the currently displayed fragment
     */
    public final void removeFragment() {
        if (getCurrentFragment() != null)
            getSupportFragmentManager().beginTransaction().remove(getCurrentFragment()).commit();
    }

    /**
     @return true if the sdk version of the device is sdkCode
     */
    public final boolean isSDK(int sdkCode) {
        return Build.VERSION.SDK_INT == sdkCode;
    }

    /**
     @return true if the sdk version of the device is greater or equal to sdkCode
     */
    public final boolean isSDKOrAbove(int sdkCode) {
        return Build.VERSION.SDK_INT >= sdkCode;
    }

    /**
     @return true if the sdk version of the device is greater than sdkCode
     */
    public final boolean isSDKAbove(int sdkCode) {
        return Build.VERSION.SDK_INT > sdkCode;
    }

    /**
     Retrieve the version name from the package name. THIS IS NOT BuildConfig.VERSION_NAME, being in a different module
     this value is wrong.

     @return the application version name (NOT the library version code)
     */
    public String getApplicationVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            Log.d(TAG, "Version name: " + packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Open the PlayStore at the specified packageName page */
    public final void openPlayStore(String packageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                                     Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                                         Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            } catch (ActivityNotFoundException e2) {

            }
        }
    }

    /** Start the application launcher activity for the specified packageName */
    public final void openApplication(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            makeSnack(R.string.snack_application_not_found).show();
        }
    }

    /** Thread-safe version of {@link android.app.Activity#finish()} */
    public final void postFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void executeTransaction(FragmentTransactionRunnable transactionRunnable) {
        transactionRunnable.run(getSupportFragmentManager(), getFragmentContainerId());
    }

    @Override
    public Snackbar makeSnack(int contentId) {
        return makeSnack(getString(contentId), Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar makeSnack(String content) {
        return makeSnack(content, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar makeSnack(int contentId, int duration) {
        return makeSnack(getString(contentId), duration);
    }

    @Override
    public Snackbar makeSnack(final String content, final int duration) {
        return Snackbar.make(findViewById(getFragmentContainerId()), content, duration);
    }

    public interface ActivityLifeCycleListener {
        void onActivityCreated();

        void onActivityResulted(int requestCode, int resultCode, Intent data);

        void onActivityResumed();

        void onActivityPaused();

        void onActivityStopped();
    }
}
