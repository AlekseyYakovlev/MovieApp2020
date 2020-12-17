package ru.spb.yakovlev.androidacademy2020.extensions

import kotlin.math.roundToInt

fun Float.roundRating() = this.roundToInt().toFloat() / 2
