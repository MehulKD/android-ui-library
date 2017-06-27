package com.github.badoualy.ui.listener

import android.support.v7.widget.Toolbar

/** Some convenience method to act on the actionbar without having to cast the activity  */
interface ToolbarHandler {

    val toolbar: Toolbar?

    /**
     * Enable/Disable the back button as the navigation icon in the ActionBar

     * @param enabled true if the HomeAsUp should be enabled
     */
    fun setHomeAsUpEnabled(enabled: Boolean)
}