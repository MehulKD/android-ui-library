package com.github.badoualy.ui.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

public abstract class FontUtils {
    private static final HashMap<String, Typeface> typefaceMap = new HashMap<>();

    public static final String FONT_DIR = "fonts/";
    public static final String TAG = FontUtils.class.getName();

    public static Typeface fromAsset(Context context, String path) {
        if (!typefaceMap.containsKey(path) && path != null) {
            Log.d(TAG, "Loading font for first time: " + path);
            if (path.equalsIgnoreCase("roboto"))
                typefaceMap.put(path, Typeface.DEFAULT);
            else
                typefaceMap.put(path, Typeface.createFromAsset(context.getAssets(), FONT_DIR + path));
        }

        return typefaceMap.get(path);
    }

    public static Typeface fromFile(String path) {
        if (!typefaceMap.containsKey(path) && path != null) {
            Log.d(TAG, "Loading font for first time: " + path);
            if (path.equalsIgnoreCase("roboto"))
                typefaceMap.put(path, Typeface.DEFAULT);
            else
                typefaceMap.put(path, Typeface.createFromFile(path));
        }

        return typefaceMap.get(path);
    }
}
