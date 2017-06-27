package com.github.badoualy.ui.util

import android.content.res.TypedArray

fun TypedArray.mapToColors() = (0 until length()).map { getColor(it, 0) }