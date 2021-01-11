package ru.spb.yakovlev.movieapp2020.model

data class MovieData(
    val id: Int = 0,
    val title: String = "",
    val genres: List<Int> = listOf(),
    val runtime: Int = 0,
    val minimumAge: String = "6+",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val backdrop: String = "",
    val isLike: Boolean = false,
    val overview: String = "",
)