package ru.spb.yakovlev.movieapp2020.data.remote.resp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("adult")
    val isAdult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String = "",
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("id")
    val id: Int = 0,
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String = "",
    @SerialName("runtime")
    val runtime: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("video")
    val video: Boolean = false,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
) {
    @Serializable
    data class Genre(
        @SerialName("name")
        val name: String = "",
    )
}