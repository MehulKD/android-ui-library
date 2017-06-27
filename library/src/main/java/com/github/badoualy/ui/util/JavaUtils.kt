package com.github.badoualy.ui.util

fun Int.colorToHex() = String.format("#%06X", 0xFFFFFF and this)
