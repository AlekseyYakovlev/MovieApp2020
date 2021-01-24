package ru.spb.yakovlev.movieapp2020.model

data class MovieDetailsData (
    val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val runtime: Int = 0,
    val certification: String = "6+",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val backdrop: String = "",
    val isLiked: Boolean = false,
    val overview: String = "",
)