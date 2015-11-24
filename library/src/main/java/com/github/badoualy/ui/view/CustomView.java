package com.github.badoualy.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 @author https://github.com/navasmdc/MaterialDesignLibrary */
abstract class CustomView extends RelativeLayout {

    private final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    private final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    protected final int disabledBackgroundColor = Color.parseColor("#E2E2E2");
    protected int beforeBackground;

    // Indicate if user touched this view the last time
    protected boolean animation = false;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomView(Context context) {
        super(context);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setBackgroundColor(enabled ? beforeBackground : disabledBackgroundColor);
        invalidate();
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        animation = true;
    }

    @Override
    protected void onAnimationEnd() {
        animation = false;
        super.onAnimationEnd();
    }

}
