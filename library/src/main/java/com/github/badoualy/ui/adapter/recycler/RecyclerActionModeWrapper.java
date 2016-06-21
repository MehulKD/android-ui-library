package com.github.badoualy.ui.adapter.recycler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.badoualy.ui.listener.NavigationDrawerHandler;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActionModeWrapper<T> implements ActionMode.Callback {

    protected final AppCompatActivity activity;
    protected final RecyclerView.Adapter adapter;
    private final ActionMode.Callback wrapped;

    private final List<T> selectedItems;
    protected ActionMode mode;

    private boolean drawerLocked;

    public RecyclerActionModeWrapper(AppCompatActivity activity, RecyclerView.Adapter adapter, ActionMode.Callback wrapped) {
        this.activity = activity;
        this.adapter = adapter;
        this.wrapped = wrapped;

        selectedItems = new ArrayList<>();
        mode = null;

        drawerLocked = ((activity instanceof NavigationDrawerHandler) && ((NavigationDrawerHandler) activity).isNavigationDrawerLocked());
    }

    public boolean isInActionMode() {
        return mode != null;
    }

    @Override
    public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
        this.mode = mode;

        if (activity instanceof NavigationDrawerHandler && !drawerLocked)
            ((NavigationDrawerHandler) activity).setNavigationDrawerLocked(true);

        if (wrapped != null)
            wrapped.onCreateActionMode(mode, menu);

        activity.onSupportActionModeStarted(mode);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return wrapped != null && wrapped.onPrepareActionMode(mode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return wrapped != null && wrapped.onActionItemClicked(mode, item);
    }

    @Override
    public final void onDestroyActionMode(ActionMode mode) {
        selectedItems.clear();
        adapter.notifyDataSetChanged();

        if (activity instanceof NavigationDrawerHandler && !drawerLocked)
            ((NavigationDrawerHandler) activity).setNavigationDrawerLocked(false);

        if (wrapped != null)
            wrapped.onDestroyActionMode(mode);

        activity.onSupportActionModeFinished(mode);
        this.mode = null;
    }

    public final void onItemTap(T item, int position) {
        if (mode == null)
            return;

        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
            if (selectedItems.isEmpty())
                mode.finish();
        } else {
            selectedItems.add(item);
        }

        if (!selectedItems.isEmpty())
            onSelectionChanged(selectedItems.size());
        adapter.notifyItemChanged(position);
    }

    protected final void setTitle(String title) {
        if (mode != null)
            mode.setTitle(title);
    }

    protected final void setSubtitle(String subtitle) {
        if (mode != null)
            mode.setSubtitle(subtitle);
    }

    /**
     * Override this method to update the title, or anything when the selection changed
     *
     * @param count total count of selected items
     */
    public void onSelectionChanged(int count) {
    }

    public boolean isItemSelected(T item) {
        return selectedItems.contains(item);
    }

    public List<T> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }
}