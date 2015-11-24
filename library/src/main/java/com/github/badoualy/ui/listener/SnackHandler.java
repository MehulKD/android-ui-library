package com.github.badoualy.ui.listener;

import android.support.design.widget.Snackbar;

public interface SnackHandler {
    Snackbar makeSnack(int contentId);

    Snackbar makeSnack(String content);

    Snackbar makeSnack(int contentId, int duration);

    Snackbar makeSnack(String content, int duration);
}
