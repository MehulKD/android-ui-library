package com.github.badoualy.ui.config;

import android.content.Intent;

public interface SplashConfig {
    /** @return The SplashActivity intent to start at first launch, null if no splash wanted */
    Intent getSplashActivityIntent();
}
