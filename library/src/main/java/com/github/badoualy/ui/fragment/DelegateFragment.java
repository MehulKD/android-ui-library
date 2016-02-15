package com.github.badoualy.ui.fragment;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/** A basic fragment to handle some basics operations, mostly delegates */
public abstract class DelegateFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    private boolean paused = true;

    public final Context getApplicationContext() {
        return getActivity() != null ? getActivity().getApplicationContext() : null;
    }

    public final Application getApplication() {
        return getActivity() != null ? getActivity().getApplication() : null;
    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    public void onPause() {
        paused = true;
        super.onPause();
    }

    /** @return the Activity this fragment is currently associated with. */
    public final AppCompatActivity getSupportActivity() {
        if (getActivity() instanceof AppCompatActivity)
            return (AppCompatActivity) getActivity();

        return null;
    }

    /**
     * Delegate getSupportActivity().getSupportActionBar() with null safety
     *
     * @return the support action bar, null if not attached to an activity
     */
    public final ActionBar getSupportActionBar() {
        AppCompatActivity activity = getSupportActivity();
        if (activity != null)
            return activity.getSupportActionBar();

        return null;
    }

    /** Close the soft input keyboard if open */
    public final void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        Activity activity = getActivity();
        if (activity != null) {
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                IBinder token = focus.getWindowToken();
                if (token != null)
                    inputMethodManager.hideSoftInputFromWindow(token, 0);
            }
        }
    }

    /**
     * @param id  the id of the view
     * @param <T> the type of the view
     * @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(int id) {
        return getView() != null ? (T) getView().findViewById(id) : null;
    }

    /**
     * @param id  the id of the view
     * @param <T> the type of the view
     * @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(int id, Class<T> clazz) {
        return getView() != null ? (T) getView().findViewById(id) : null;
    }

    /**
     * @param root the root of the view
     * @param id   the id of the view
     * @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(View root, int id) {
        return (T) root.findViewById(id);
    }

    /**
     * @param root the root of the view
     * @param id   the id of the view
     * @param <T>  the type of the view
     * @return {@link View#findViewById(int)} with the supplied id, and cast it into T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T viewById(View root, int id, Class<T> clazz) {
        return (T) root.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    /**
     @param <T>  the type of the service
     @return {@link Context#getSystemService(String)} with the given name and cast it into T */
    public final <T> T getSystemService(String name) {
        return (T) getActivity().getSystemService(name);
    }

    @SuppressWarnings("unchecked")
    /**
     @param <T>  the type of the service
     @param clazz the class of the service
     @return {@link Context#getSystemService(String)} with the given name and cast it into T */
    public final <T> T getSystemService(String name, Class<T> clazz) {
        return (T) getActivity().getSystemService(name);
    }

    /**
     * Delegate getSupportActionBar().setTitle(title)
     *
     * @param title the new title
     */
    protected void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        else
            Log.d(TAG, "Trying to set title on null SupportActionBar");
    }

    /**
     * Delegate getSupportActionBar().setTitle(title)
     *
     * @param subtitle the new subtitle
     */
    protected void setSubtitle(String subtitle) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(subtitle);
        else
            Log.d(TAG, "Trying to set subtitle on null SupportActionBar");
    }


    /** Delegate getContext().getSupportFragmentManager().popBackStack() */
    public final void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /** Thread-safe finish */
    public final void postFinish() {
        if (getView() != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }

    /**
     * Delegate getContext().getResources().getStringArray(id)
     *
     * @param id
     * @return
     */
    protected final String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * Delegate getContext().getResources().getQuantityString(id, quantity)
     *
     * @param id
     * @param quantity
     * @return
     */
    protected final String getQuantityString(int id, int quantity) {
        return getContext().getResources().getQuantityString(id, quantity);
    }

    /**
     * Delegate getContext().getResources().getColor(id)
     *
     * @param id
     * @return
     */
    protected final int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getContext().getResources().getColor(id, getContext().getTheme());
        return getContext().getResources().getColor(id);
    }

    /**
     * Delegate getContext().getDrawable(id)
     *
     * @param id
     * @return
     */
    protected final Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getContext().getResources().getDrawable(id, getContext().getTheme());
        return getContext().getResources().getDrawable(id);
    }

    /**
     * Delegate getContext().getResources().getDimensionPixelSize(id)
     *
     * @param id
     * @return
     */
    protected final int getDimensionPixelSize(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    public boolean isPaused() {
        return paused;
    }
}
