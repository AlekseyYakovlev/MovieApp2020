package ru.spb.yakovlev.movieapp2020.data.db

import androidx.room.TypeConverter

class ListConverter {
    companion object {
        const val SPLITTING_SYMBOLS = ";"
    }

    @TypeConverter
    fun String.toListOfInts(): List<Int> =
        if (isNotBlank()) this.split(SPLITTING_SYMBOLS).map { it.toInt() }
        else emptyList()

    @TypeConverter
    fun List<Int>?.toStringConverter(): String =
        this?.joinToString(separator = SPLITTING_SYMBOLS) { it.toString() } ?: ""
}