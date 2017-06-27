package com.github.badoualy.ui.config

interface AppConfig : AppBuildConfig {
    val appName: String
    val primaryColor: Int
    val primaryDarkColor: Int
    val primaryLightColor: Int
    val accentColor: Int
    val versionName: String
}
