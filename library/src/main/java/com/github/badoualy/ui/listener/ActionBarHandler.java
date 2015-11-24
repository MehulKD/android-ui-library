package com.github.badoualy.ui.listener;

import android.support.v7.widget.Toolbar;

/** Some convenience method to act on the actionbar without having to cast the activity */
public interface ActionBarHandler {

    /** Enable/Disable the back button as the navigation icon in the ActionBar

     @param enabled true if the HomeAsUp should be enabled
     */
    void setHomeAsUpEnabled(boolean enabled);

    /** @return The toolbar used as ActionBar or null */
    Toolbar getToolbar();
}
