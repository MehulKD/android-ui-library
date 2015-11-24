package com.github.badoualy.ui.listener;

/** Some convenience method to act on the NavigationDrawer without having to cast the activity */
public interface NavigationDrawerHandler {

    /** Lock/Unlock the NavigationDrawer to disable/enable it's opening
     @param locked true if the drawer should be locked
     */
    void setNavigationDrawerLocked(boolean locked);

    /** @return true if the NavigationDrawer is locked, false if not */
    boolean isNavigationDrawerLocked();
}
