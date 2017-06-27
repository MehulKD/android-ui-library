package com.github.badoualy.ui.util

fun Float.convertRange(oldMin: Float, oldMax: Float, newMin: Float, newMax: Float): Float {
    val oldRange = oldMax - oldMin
    if (oldRange == 0f)
        return newMin

    val newRange = newMax - newMin
    return (this - oldMin) * newRange / oldRange + newMin
}

fun Int.convertRange(oldMin: Int, oldMax: Int, newMin: Int, newMax: Int): Int {
    val oldRange = oldMax - oldMin
    if (oldRange == 0)
        return newMin

    val newRange = newMax - newMin
    return (this - oldMin) * newRange / oldRange + newMin
}

fun Double.convertRange(oldMin: Double, oldMax: Double, newMin: Double, newMax: Double): Double {
    val oldRange = oldMax - oldMin
    if (oldRange == 0.0)
        return newMin

    val newRange = newMax - newMin
    return (this - oldMin) * newRange / oldRange + newMin
}