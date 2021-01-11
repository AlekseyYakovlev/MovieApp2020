package ru.spb.yakovlev.movieapp2020.model

data class ApiSettings(
    val secureBaseUrl: String = "https://image.tmdb.org/t/p/",
    val backdropSize: String = "w1280/",
    val posterSize: String = "w342/",
    val profileSize: String = "w185/",
)
