package com.github.badoualy.ui.config

/** Provide some information about the build of the app, useful for external modules  */
interface AppBuildConfig {
    val isDebug: Boolean
    val isRelease: Boolean
}