package ru.spb.yakovlev.movieapp2020.model

data class MovieData(
    val id: Int = 0,
    val title: String = "",
    val genres: String = "",
    val runtime: Int = 0,
    val minimumAge: String = "6+",
    val rating: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val poster2: String = "",
    val isLike: Boolean = false,
    val overview: String = "",
    val castIds: List<Int> = listOf(),
)

fun MovieData.toMovieItemData() = MovieItemData(
    id = id,
    title = title,
    genre = genres,
    runtime = runtime,
    minimumAge = minimumAge,
    ratings = rating,
    numberOfRatings = numberOfRatings,
    poster = poster,
    isLike = isLike,
)