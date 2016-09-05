package com.github.badoualy.ui.util;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.badoualy.ui.config.AppConfig;

public final class DialogUtils {

    private DialogUtils() {

    }

    public static MaterialDialog.Builder newDialog(@NonNull Activity activity) {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        final Application application = activity.getApplication();
        if (application instanceof AppConfig) {
            final AppConfig config = (AppConfig) application;
            builder.positiveColor(config.getAccentColor());
            builder.negativeColor(config.getAccentColor());
        }

        return builder;
    }
}
