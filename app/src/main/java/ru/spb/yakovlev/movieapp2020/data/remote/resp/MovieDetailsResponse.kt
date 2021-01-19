package ru.spb.yakovlev.movieapp2020.data.remote.resp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("adult")
    val isAdult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String = "",
    @SerialName("budget")
    val budget: Int = 0,
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("homepage")
    val homepage: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("imdb_id")
    val imdbId: String = "",
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String = "",
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("revenue")
    val revenue: Int = 0,
    @SerialName("runtime")
    val runtime: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("tagline")
    val tagline: String = "",
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
        @SerialName("id")
        val id: Int = 0,
        @SerialName("name")
        val name: String = "",
    )
}