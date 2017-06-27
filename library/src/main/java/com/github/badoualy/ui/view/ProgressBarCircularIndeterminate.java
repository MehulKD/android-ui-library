package com.github.badoualy.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * @author https://github.com/navasmdc/MaterialDesignLibrary
 */
public class ProgressBarCircularIndeterminate extends CustomView {

    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    private int backgroundColor = Color.parseColor("#1E88E5");

    private int cont = 0;
    private float radius1 = 0;
    private float radius2 = 0;
    private boolean firstAnimationOver = false;

    private int arcD = 1;
    private int arcO = 0;
    private float rotateAngle = 0;
    private int limit = 0;

    private Bitmap bitmap;
    private Canvas myCanvas;
    private Bitmap bitmap2;
    private Canvas myCanvas2;
    private Paint pressPaint, transparentPaint, defaultPaint, backgroundPaint;
    private int dp4Px;

    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setMinimumHeight(dpToPx(32, getResources()));
        setMinimumWidth(dpToPx(32, getResources()));

        // Set background Color
        // Color by resource
        int bgColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bgColor != -1) {
            setBackgroundColor(getResources().getColor(bgColor));
        } else {
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            setBackgroundColor(background != -1 ? background : Color.parseColor("#1E88E5"));
        }

        setMinimumHeight(dpToPx(3, getResources()));
        init2();
    }

    private void init2() {
        pressPaint = new Paint();
        pressPaint.setAntiAlias(true);
        pressPaint.setColor(makePressColor());

        transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);

        defaultPaint = new Paint();
        dp4Px = dpToPx(4, getResources());
    }

    public void reset() {
        firstAnimationOver = false;
        cont = 0;
        radius1 = 0;
        radius2 = 0;
        arcD = 1;
        arcO = 0;
        rotateAngle = 0;
        limit = 0;
    }

    // Set color of background
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        if (isEnabled()) {
            beforeBackground = backgroundColor;
        }
        this.backgroundColor = color;
    }

    /**
     * Make a dark color to ripple effect
     */
    protected int makePressColor() {
        int r = (backgroundColor >> 16) & 0xFF;
        int g = (backgroundColor >> 8) & 0xFF;
        int b = (backgroundColor) & 0xFF;
        //		r = (r+90 > 245) ? 245 : r+90;
        //		g = (g+90 > 245) ? 245 : g+90;
        //		b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128, r, g, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(bitmap);

        bitmap2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        myCanvas2 = new Canvas(bitmap2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!firstAnimationOver) {
            drawFirstAnimation(canvas);
        }
        if (cont > 0) {
            drawSecondAnimation(canvas);
        }
        invalidate();
    }

    private void drawFirstAnimation(Canvas canvas) {
        if (radius1 < getWidth() / 2) {
            radius1 = Math.min(radius1 + 1, (float) getWidth() / 2);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius1, pressPaint);
        } else {
            bitmap.eraseColor(Color.TRANSPARENT);
            myCanvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, pressPaint);

            float value = (float) getWidth() / 2;
            if (cont < 50) {
                value -= dp4Px;
            }
            radius2 = Math.min(radius2 + 1, value);

            myCanvas.drawCircle(getWidth() / 2, getHeight() / 2, radius2, transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, defaultPaint);

            if (radius2 >= getWidth() / 2 - dp4Px) {
                cont++;
            }
            if (radius2 >= getWidth() / 2) {
                firstAnimationOver = true;
            }
        }
    }

    private void drawSecondAnimation(Canvas canvas) {
        if (arcO == limit) {
            arcD += 6;
        }
        if (arcD >= 290 || arcO > limit) {
            arcO += 6;
            arcD -= 6;
        }
        if (arcO > limit + 290) {
            limit = arcO;
            arcO = limit;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        bitmap2.eraseColor(Color.TRANSPARENT);
        myCanvas2.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, backgroundPaint);

        myCanvas2.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - dpToPx(4, getResources()),
                             transparentPaint);

        canvas.drawBitmap(bitmap2, 0, 0, defaultPaint);
    }

    static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}