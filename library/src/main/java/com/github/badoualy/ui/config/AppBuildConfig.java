package com.github.badoualy.ui.config;

/** Provide some information about the build of the app, useful for external mdules */
public interface AppBuildConfig {

    /** @return true if the app is debug build */
    boolean isDebug();

    /** @return true if the app is release build */
    boolean isRelease();
}
