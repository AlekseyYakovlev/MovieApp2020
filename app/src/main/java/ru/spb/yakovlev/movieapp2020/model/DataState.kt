package ru.spb.yakovlev.movieapp2020.model

sealed class DataState<out T : Any> {
    object Empty : DataState<Nothing>()
    data class Loading(val progress: Int?) : DataState<Nothing>()
    data class Success<out T : Any>(val data: T) : DataState<T>()
    data class Error(val errorMessage: String) : DataState<Nothing>()
}