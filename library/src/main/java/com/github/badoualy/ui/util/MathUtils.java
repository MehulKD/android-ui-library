package com.github.badoualy.ui.util;

public final class MathUtils {

    private MathUtils() {

    }

    public static float switchRange(float oldMin, float oldMax, float newMin, float newMax, float value) {
        float oldRange = oldMax - oldMin;
        if (oldRange == 0)
            return newMin;

        float newRange = newMax - newMin;
        return (((value - oldMin) * newRange) / oldRange) + newMin;
    }

    /**
     * This function returns the result of linearly interpolating the start and end values, with
     * <code>fraction</code> representing the proportion between the start and end values. The
     * calculation is a simple parametric calculation: <code>result = x0 + t * (v1 - v0)</code>,
     * where <code>x0</code> is <code>startValue</code>, <code>x1</code> is <code>endValue</code>,
     * and <code>t</code> is <code>fraction</code>.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start value
     * @param endValue   The end value
     * @return A linear interpolation between the start and end values, given the
     * <code>fraction</code> parameter.
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        return (int) (startValue + fraction * (endValue - startValue));
    }


    /**
     * This function returns the result of linearly interpolating the start and end values, with
     * <code>fraction</code> representing the proportion between the start and end values. The
     * calculation is a simple parametric calculation: <code>result = x0 + t * (v1 - v0)</code>,
     * where <code>x0</code> is <code>startValue</code>, <code>x1</code> is <code>endValue</code>,
     * and <code>t</code> is <code>fraction</code>.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start value
     * @param endValue   The end value
     * @return A linear interpolation between the start and end values, given the
     * <code>fraction</code> parameter.
     */
    public static float evaluate(float fraction, float startValue, float endValue) {
        return startValue + fraction * (endValue - startValue);
    }

    public static int evaluateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24);
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24);
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }
}
