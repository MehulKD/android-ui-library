package com.github.badoualy.ui.config;

public interface AppConfig extends AppBuildConfig {
    String getAppName();

    int getPrimaryColor();

    int getPrimaryDarkColor();

    int getPrimaryLightColor();

    int getAccentColor();

    String getVersionName();
}
